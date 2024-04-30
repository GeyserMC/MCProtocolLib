package org.geysermc.mcprotocollib.protocol;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.auth.SessionService;
import org.geysermc.mcprotocollib.auth.exception.request.RequestException;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.ConnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.DisconnectingEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.data.ProtocolState;
import org.geysermc.mcprotocollib.protocol.data.game.RegistryEntry;
import org.geysermc.mcprotocollib.protocol.data.status.PlayerInfo;
import org.geysermc.mcprotocollib.protocol.data.status.ServerStatusInfo;
import org.geysermc.mcprotocollib.protocol.data.status.VersionInfo;
import org.geysermc.mcprotocollib.protocol.data.status.handler.ServerInfoBuilder;
import org.geysermc.mcprotocollib.protocol.packet.common.clientbound.ClientboundDisconnectPacket;
import org.geysermc.mcprotocollib.protocol.packet.common.clientbound.ClientboundKeepAlivePacket;
import org.geysermc.mcprotocollib.protocol.packet.common.serverbound.ServerboundKeepAlivePacket;
import org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound.ClientboundFinishConfigurationPacket;
import org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound.ClientboundRegistryDataPacket;
import org.geysermc.mcprotocollib.protocol.packet.configuration.serverbound.ServerboundFinishConfigurationPacket;
import org.geysermc.mcprotocollib.protocol.packet.handshake.serverbound.ClientIntentionPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundConfigurationAcknowledgedPacket;
import org.geysermc.mcprotocollib.protocol.packet.login.clientbound.ClientboundGameProfilePacket;
import org.geysermc.mcprotocollib.protocol.packet.login.clientbound.ClientboundHelloPacket;
import org.geysermc.mcprotocollib.protocol.packet.login.clientbound.ClientboundLoginCompressionPacket;
import org.geysermc.mcprotocollib.protocol.packet.login.clientbound.ClientboundLoginDisconnectPacket;
import org.geysermc.mcprotocollib.protocol.packet.login.serverbound.ServerboundHelloPacket;
import org.geysermc.mcprotocollib.protocol.packet.login.serverbound.ServerboundKeyPacket;
import org.geysermc.mcprotocollib.protocol.packet.login.serverbound.ServerboundLoginAcknowledgedPacket;
import org.geysermc.mcprotocollib.protocol.packet.status.clientbound.ClientboundPongResponsePacket;
import org.geysermc.mcprotocollib.protocol.packet.status.clientbound.ClientboundStatusResponsePacket;
import org.geysermc.mcprotocollib.protocol.packet.status.serverbound.ServerboundPingRequestPacket;
import org.geysermc.mcprotocollib.protocol.packet.status.serverbound.ServerboundStatusRequestPacket;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Handles initial login and status requests for servers.
 */
public class ServerListener extends SessionAdapter {
    private static final int DEFAULT_COMPRESSION_THRESHOLD = 256;

    // Always empty post-1.7
    private static final String SERVER_ID = "";
    private static final KeyPair KEY_PAIR;

    static {
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(1024);
            KEY_PAIR = gen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Failed to generate server key pair.", e);
        }
    }

    private final NbtMap networkCodec;

    private final byte[] challenge = new byte[4];
    private String username = "";

    private long lastPingTime = 0;
    private int lastPingId = 0;

    public ServerListener(NbtMap networkCodec) {
        this.networkCodec = networkCodec;
        new Random().nextBytes(this.challenge);
    }

    @Override
    public void connected(ConnectedEvent event) {
        event.getSession().setFlag(MinecraftConstants.PING_KEY, 0L);
    }

    @Override
    public void packetReceived(Session session, Packet packet) {
        MinecraftProtocol protocol = (MinecraftProtocol) session.getPacketProtocol();
        if (protocol.getState() == ProtocolState.HANDSHAKE) {
            if (packet instanceof ClientIntentionPacket intentionPacket) {
                switch (intentionPacket.getIntent()) {
                    case STATUS -> protocol.setState(ProtocolState.STATUS);
                    case TRANSFER -> {
                        if (!session.getFlag(MinecraftConstants.ACCEPT_TRANSFERS_KEY, false)) {
                            session.disconnect("Server does not accept transfers.");
                        }
                    }
                    case LOGIN -> {
                        protocol.setState(ProtocolState.LOGIN);
                        if (intentionPacket.getProtocolVersion() > protocol.getCodec().getProtocolVersion()) {
                            session.disconnect("Outdated server! I'm still on " + protocol.getCodec().getMinecraftVersion() + ".");
                        } else if (intentionPacket.getProtocolVersion() < protocol.getCodec().getProtocolVersion()) {
                            session.disconnect("Outdated client! Please use " + protocol.getCodec().getMinecraftVersion() + ".");
                        }
                    }
                    default -> throw new UnsupportedOperationException("Invalid client intent: " + intentionPacket.getIntent());
                }
            }
        } else if (protocol.getState() == ProtocolState.LOGIN) {
            if (packet instanceof ServerboundHelloPacket helloPacket) {
                this.username = helloPacket.getUsername();

                if (session.getFlag(MinecraftConstants.VERIFY_USERS_KEY, true)) {
                    session.send(new ClientboundHelloPacket(SERVER_ID, KEY_PAIR.getPublic(), this.challenge, true));
                } else {
                    new Thread(new UserAuthTask(session, null)).start();
                }
            } else if (packet instanceof ServerboundKeyPacket keyPacket) {
                PrivateKey privateKey = KEY_PAIR.getPrivate();

                if (!Arrays.equals(this.challenge, keyPacket.getEncryptedChallenge(privateKey))) {
                    session.disconnect("Invalid challenge!");
                    return;
                }

                SecretKey key = keyPacket.getSecretKey(privateKey);
                session.enableEncryption(protocol.enableEncryption(key));
                new Thread(new UserAuthTask(session, key)).start();
            } else if (packet instanceof ServerboundLoginAcknowledgedPacket) {
                protocol.setState(ProtocolState.CONFIGURATION);

                // Credit ViaVersion: https://github.com/ViaVersion/ViaVersion/blob/dev/common/src/main/java/com/viaversion/viaversion/protocols/protocol1_20_5to1_20_3/rewriter/EntityPacketRewriter1_20_5.java
                for (Map.Entry<String, Object> entry : networkCodec.entrySet()) {
                    NbtMap entryTag = (NbtMap) entry.getValue();
                    String typeTag = entryTag.getString("type");
                    List<NbtMap> valueTag = entryTag.getList("value", NbtType.COMPOUND);
                    List<RegistryEntry> entries = new ArrayList<>();
                    for (NbtMap compoundTag : valueTag) {
                        String nameTag = compoundTag.getString("name");
                        int id = compoundTag.getInt("id");
                        entries.add(id, new RegistryEntry(nameTag, compoundTag.getCompound("element")));
                    }

                    session.send(new ClientboundRegistryDataPacket(typeTag, entries));
                }

                session.send(new ClientboundFinishConfigurationPacket());
            }
        } else if (protocol.getState() == ProtocolState.STATUS) {
            if (packet instanceof ServerboundStatusRequestPacket) {
                ServerInfoBuilder builder = session.getFlag(MinecraftConstants.SERVER_INFO_BUILDER_KEY);
                if (builder == null) {
                    builder = $ -> new ServerStatusInfo(
                            new VersionInfo(protocol.getCodec().getMinecraftVersion(), protocol.getCodec().getProtocolVersion()),
                            new PlayerInfo(0, 20, new ArrayList<>()),
                            Component.text("A Minecraft Server"),
                            null,
                            false
                    );
                }

                ServerStatusInfo info = builder.buildInfo(session);
                session.send(new ClientboundStatusResponsePacket(info));
            } else if (packet instanceof ServerboundPingRequestPacket pingRequestPacket) {
                session.send(new ClientboundPongResponsePacket(pingRequestPacket.getPingTime()));
            }
        } else if (protocol.getState() == ProtocolState.GAME) {
            if (packet instanceof ServerboundKeepAlivePacket keepAlivePacket) {
                if (keepAlivePacket.getPingId() == this.lastPingId) {
                    long time = System.currentTimeMillis() - this.lastPingTime;
                    session.setFlag(MinecraftConstants.PING_KEY, time);
                }
            } else if (packet instanceof ServerboundConfigurationAcknowledgedPacket) {
                protocol.setState(ProtocolState.CONFIGURATION);
            } else if (packet instanceof ServerboundPingRequestPacket pingRequestPacket) {
                session.send(new ClientboundPongResponsePacket(pingRequestPacket.getPingTime()));
            }
        } else if (protocol.getState() == ProtocolState.CONFIGURATION) {
            if (packet instanceof ServerboundFinishConfigurationPacket) {
                protocol.setState(ProtocolState.GAME);
                ServerLoginHandler handler = session.getFlag(MinecraftConstants.SERVER_LOGIN_HANDLER_KEY);
                if (handler != null) {
                    handler.loggedIn(session);
                }

                if (session.getFlag(MinecraftConstants.AUTOMATIC_KEEP_ALIVE_MANAGEMENT, true)) {
                    new Thread(new KeepAliveTask(session)).start();
                }
            }
        }
    }

    @Override
    public void packetSent(Session session, Packet packet) {
        if (packet instanceof ClientboundLoginCompressionPacket loginCompressionPacket) {
            session.setCompressionThreshold(loginCompressionPacket.getThreshold(), true);
            session.send(new ClientboundGameProfilePacket(session.getFlag(MinecraftConstants.PROFILE_KEY), true));
        }
    }

    @Override
    public void disconnecting(DisconnectingEvent event) {
        MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
        if (protocol.getState() == ProtocolState.LOGIN) {
            event.getSession().send(new ClientboundLoginDisconnectPacket(event.getReason()));
        } else if (protocol.getState() == ProtocolState.GAME) {
            event.getSession().send(new ClientboundDisconnectPacket(event.getReason()));
        }
    }

    @RequiredArgsConstructor
    private class UserAuthTask implements Runnable {
        private final Session session;
        private final SecretKey key;

        @Override
        public void run() {
            GameProfile profile;
            if (this.key != null) {
                SessionService sessionService = this.session.getFlag(MinecraftConstants.SESSION_SERVICE_KEY, new SessionService());
                try {
                    profile = sessionService.getProfileByServer(username, SessionService.getServerId(SERVER_ID, KEY_PAIR.getPublic(), this.key));
                } catch (RequestException e) {
                    this.session.disconnect("Failed to make session service request.", e);
                    return;
                }

                if (profile == null) {
                    this.session.disconnect("Failed to verify username.");
                }
            } else {
                profile = new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes()), username);
            }

            this.session.setFlag(MinecraftConstants.PROFILE_KEY, profile);

            int threshold = session.getFlag(MinecraftConstants.SERVER_COMPRESSION_THRESHOLD, DEFAULT_COMPRESSION_THRESHOLD);
            this.session.send(new ClientboundLoginCompressionPacket(threshold));
        }
    }

    @RequiredArgsConstructor
    private class KeepAliveTask implements Runnable {
        private final Session session;

        @Override
        public void run() {
            while (this.session.isConnected()) {
                lastPingTime = System.currentTimeMillis();
                lastPingId = (int) lastPingTime;
                this.session.send(new ClientboundKeepAlivePacket(lastPingId));

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
