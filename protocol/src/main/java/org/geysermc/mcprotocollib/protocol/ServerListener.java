package org.geysermc.mcprotocollib.protocol;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.service.SessionService;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
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
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

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

    private boolean keepAlivePending;
    private long keepAliveChallenge;
    private long keepAliveTime;
    @Getter
    private boolean isTransfer = false;

    public ServerListener(NbtMap networkCodec) {
        this.networkCodec = networkCodec;
        ThreadLocalRandom.current().nextBytes(this.challenge);
    }

    @Override
    public void connected(ConnectedEvent event) {
        event.getSession().setFlag(MinecraftConstants.PING_KEY, 0L);
    }

    @Override
    public void packetReceived(Session session, Packet packet) {
        MinecraftProtocol protocol = (MinecraftProtocol) session.getPacketProtocol();
        if (protocol.getInboundState() == ProtocolState.HANDSHAKE) {
            if (packet instanceof ClientIntentionPacket intentionPacket) {
                switch (intentionPacket.getIntent()) {
                    case STATUS -> {
                        session.switchOutboundProtocol(() -> protocol.setOutboundState(ProtocolState.STATUS));
                        session.switchInboundProtocol(() -> protocol.setInboundState(ProtocolState.STATUS));
                    }
                    case TRANSFER -> {
                        if (session.getFlag(MinecraftConstants.ACCEPT_TRANSFERS_KEY, false)) {
                            beginLogin(session, protocol, intentionPacket, true);
                        } else {
                            session.switchOutboundProtocol(() -> protocol.setOutboundState(ProtocolState.LOGIN));
                            session.disconnect("Server does not accept transfers.");
                        }
                    }
                    case LOGIN -> {
                        beginLogin(session, protocol, intentionPacket, false);
                    }
                    default -> throw new UnsupportedOperationException("Invalid client intent: " + intentionPacket.getIntent());
                }
            }
        } else if (protocol.getInboundState() == ProtocolState.LOGIN) {
            if (packet instanceof ServerboundHelloPacket helloPacket) {
                this.username = helloPacket.getUsername();

                if (session.getFlag(MinecraftConstants.VERIFY_USERS_KEY, true)) {
                    session.send(new ClientboundHelloPacket(SERVER_ID, KEY_PAIR.getPublic(), this.challenge, true));
                } else {
                    new Thread(() -> authenticate(session, null)).start();
                }
            } else if (packet instanceof ServerboundKeyPacket keyPacket) {
                PrivateKey privateKey = KEY_PAIR.getPrivate();

                if (!Arrays.equals(this.challenge, keyPacket.getEncryptedChallenge(privateKey))) {
                    session.disconnect("Invalid challenge!");
                    return;
                }

                SecretKey key = keyPacket.getSecretKey(privateKey);
                session.enableEncryption(protocol.enableEncryption(key));
                new Thread(() -> authenticate(session, key)).start();
            } else if (packet instanceof ServerboundLoginAcknowledgedPacket) {
                session.switchOutboundProtocol(() -> protocol.setOutboundState(ProtocolState.CONFIGURATION));
                session.switchInboundProtocol(() -> protocol.setInboundState(ProtocolState.CONFIGURATION));

                // Credit ViaVersion: https://github.com/ViaVersion/ViaVersion/blob/dev/common/src/main/java/com/viaversion/viaversion/protocols/protocol1_20_5to1_20_3/rewriter/EntityPacketRewriter1_20_5.java
                for (Map.Entry<String, Object> entry : networkCodec.entrySet()) {
                    @SuppressWarnings("PatternValidation")
                    NbtMap entryTag = (NbtMap) entry.getValue();
                    @SuppressWarnings("PatternValidation")
                    Key typeTag = Key.key(entryTag.getString("type"));
                    List<NbtMap> valueTag = entryTag.getList("value", NbtType.COMPOUND);
                    List<RegistryEntry> entries = new ArrayList<>();
                    for (NbtMap compoundTag : valueTag) {
                        @SuppressWarnings("PatternValidation")
                        Key nameTag = Key.key(compoundTag.getString("name"));
                        int id = compoundTag.getInt("id");
                        entries.add(id, new RegistryEntry(nameTag, compoundTag.getCompound("element")));
                    }

                    session.send(new ClientboundRegistryDataPacket(typeTag, entries));
                }

                session.send(new ClientboundFinishConfigurationPacket());
            }
        } else if (protocol.getInboundState() == ProtocolState.STATUS) {
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
        } else if (protocol.getInboundState() == ProtocolState.GAME) {
            if (packet instanceof ServerboundKeepAlivePacket keepAlivePacket) {
                if (keepAlivePending && keepAlivePacket.getPingId() == this.keepAliveChallenge) {
                    keepAlivePending = false;
                    session.setFlag(MinecraftConstants.PING_KEY, System.currentTimeMillis() - this.keepAliveTime);
                } else {
                    session.disconnect(Component.translatable("disconnect.timeout"));
                }
            } else if (packet instanceof ServerboundConfigurationAcknowledgedPacket) {
                // The developer who sends ClientboundStartConfigurationPacket needs to setOutboundState to CONFIGURATION
                // after sending the packet. We can't do it in this class because it needs to be a method call right after it was sent.
                // Using nettys event loop to change outgoing state may cause differences to vanilla.
                session.switchInboundProtocol(() -> protocol.setInboundState(ProtocolState.CONFIGURATION));
            } else if (packet instanceof ServerboundPingRequestPacket pingRequestPacket) {
                session.send(new ClientboundPongResponsePacket(pingRequestPacket.getPingTime()));
            }
        } else if (protocol.getInboundState() == ProtocolState.CONFIGURATION) {
            if (packet instanceof ServerboundFinishConfigurationPacket) {
                session.switchOutboundProtocol(() -> protocol.setOutboundState(ProtocolState.GAME));
                ServerLoginHandler handler = session.getFlag(MinecraftConstants.SERVER_LOGIN_HANDLER_KEY);
                if (handler != null) {
                    handler.loggedIn(session);
                }

                if (session.getFlag(MinecraftConstants.AUTOMATIC_KEEP_ALIVE_MANAGEMENT, true)) {
                    new Thread(() -> keepAlive(session)).start();
                }
            }
        }
    }

    private void beginLogin(Session session, MinecraftProtocol protocol, ClientIntentionPacket packet, boolean transferred) {
        isTransfer = transferred;
        session.switchOutboundProtocol(() -> protocol.setOutboundState(ProtocolState.LOGIN));
        if (packet.getProtocolVersion() > protocol.getCodec().getProtocolVersion()) {
            session.disconnect(Component.translatable("multiplayer.disconnect.incompatible", Component.text(protocol.getCodec().getMinecraftVersion())));
        } else if (packet.getProtocolVersion() < protocol.getCodec().getProtocolVersion()) {
            session.disconnect(Component.translatable("multiplayer.disconnect.outdated_client", Component.text(protocol.getCodec().getMinecraftVersion())));
        } else {
            session.switchInboundProtocol(() -> protocol.setInboundState(ProtocolState.LOGIN));
        }
    }

    @Override
    public void disconnecting(DisconnectingEvent event) {
        Session session = event.getSession();
        MinecraftProtocol protocol = (MinecraftProtocol) session.getPacketProtocol();
        if (protocol.getOutboundState() == ProtocolState.LOGIN) {
            session.send(new ClientboundLoginDisconnectPacket(event.getReason()));
        } else if (protocol.getOutboundState() == ProtocolState.GAME) {
            session.send(new ClientboundDisconnectPacket(event.getReason()));
        }
    }

    private void authenticate(Session session, SecretKey key) {
        GameProfile profile;
        if (key != null) {
            SessionService sessionService = session.getFlag(MinecraftConstants.SESSION_SERVICE_KEY, new SessionService());
            try {
                profile = sessionService.getProfileByServer(username, sessionService.getServerId(SERVER_ID, KEY_PAIR.getPublic(), key));
            } catch (RequestException e) {
                session.disconnect("Failed to make session service request.", e);
                return;
            }

            if (profile == null) {
                session.disconnect("Failed to verify username.");
            }
        } else {
            profile = new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8)), username);
        }

        session.setFlag(MinecraftConstants.PROFILE_KEY, profile);

        int threshold = session.getFlag(MinecraftConstants.SERVER_COMPRESSION_THRESHOLD, DEFAULT_COMPRESSION_THRESHOLD);
        if (threshold >= 0) {
            session.send(new ClientboundLoginCompressionPacket(threshold), () ->
                session.setCompressionThreshold(threshold, true));
        }

        session.send(new ClientboundGameProfilePacket(session.getFlag(MinecraftConstants.PROFILE_KEY), true));
    }

    private void keepAlive(Session session) {
        while (session.isConnected()) {
            if (keepAlivePending) {
                session.disconnect(Component.translatable("disconnect.timeout"));
                break;
            }

            long time = System.currentTimeMillis();

            keepAlivePending = true;
            keepAliveChallenge = time;
            keepAliveTime = time;
            session.send(new ClientboundKeepAlivePacket(keepAliveChallenge));

            // TODO: Implement proper tick loop rather than sleeping
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
