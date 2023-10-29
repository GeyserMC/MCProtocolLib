package com.github.steveice10.mc.protocol;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.service.SessionService;
import com.github.steveice10.mc.protocol.data.ProtocolState;
import com.github.steveice10.mc.protocol.data.status.PlayerInfo;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.VersionInfo;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoBuilder;
import com.github.steveice10.mc.protocol.packet.common.clientbound.ClientboundDisconnectPacket;
import com.github.steveice10.mc.protocol.packet.common.clientbound.ClientboundKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.common.serverbound.ServerboundKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.configuration.clientbound.ClientboundFinishConfigurationPacket;
import com.github.steveice10.mc.protocol.packet.configuration.clientbound.ClientboundRegistryDataPacket;
import com.github.steveice10.mc.protocol.packet.configuration.serverbound.ServerboundFinishConfigurationPacket;
import com.github.steveice10.mc.protocol.packet.handshake.serverbound.ClientIntentionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundConfigurationAcknowledgedPacket;
import com.github.steveice10.mc.protocol.packet.login.clientbound.ClientboundGameProfilePacket;
import com.github.steveice10.mc.protocol.packet.login.clientbound.ClientboundHelloPacket;
import com.github.steveice10.mc.protocol.packet.login.clientbound.ClientboundLoginCompressionPacket;
import com.github.steveice10.mc.protocol.packet.login.clientbound.ClientboundLoginDisconnectPacket;
import com.github.steveice10.mc.protocol.packet.login.serverbound.ServerboundHelloPacket;
import com.github.steveice10.mc.protocol.packet.login.serverbound.ServerboundKeyPacket;
import com.github.steveice10.mc.protocol.packet.login.serverbound.ServerboundLoginAcknowledgedPacket;
import com.github.steveice10.mc.protocol.packet.status.clientbound.ClientboundPongResponsePacket;
import com.github.steveice10.mc.protocol.packet.status.clientbound.ClientboundStatusResponsePacket;
import com.github.steveice10.mc.protocol.packet.status.serverbound.ServerboundPingRequestPacket;
import com.github.steveice10.mc.protocol.packet.status.serverbound.ServerboundStatusRequestPacket;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectingEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
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

    private final CompoundTag networkCodec;

    private final byte[] challenge = new byte[4];
    private String username = "";

    private long lastPingTime = 0;
    private int lastPingId = 0;

    public ServerListener(CompoundTag networkCodec) {
        this.networkCodec = networkCodec;
        ThreadLocalRandom.current().nextBytes(this.challenge);
    }

    @Override
    public void connected(ConnectedEvent event) {
        event.session().setFlag(MinecraftConstants.PING_KEY, 0L);
    }

    @Override
    public void packetReceived(Session session, Packet packet) {
        MinecraftProtocol protocol = (MinecraftProtocol) session.getPacketProtocol();
        if (protocol.getState() == ProtocolState.HANDSHAKE) {
            if (packet instanceof ClientIntentionPacket intentionPacket) {
                switch (intentionPacket.getIntent()) {
                    case STATUS -> protocol.setState(ProtocolState.STATUS);
                    case LOGIN -> {
                        protocol.setState(ProtocolState.LOGIN);
                        if (intentionPacket.getProtocolVersion() > protocol.getCodec().getProtocolVersion()) {
                            session.disconnect("Outdated server! I'm still on " + protocol.getCodec().getMinecraftVersion() + ".");
                        } else if (intentionPacket.getProtocolVersion() < protocol.getCodec().getProtocolVersion()) {
                            session.disconnect("Outdated client! Please use " + protocol.getCodec().getMinecraftVersion() + ".");
                        }
                    }
                    default ->
                            throw new UnsupportedOperationException("Invalid client intent: " + intentionPacket.getIntent());
                }
            }
        } else if (protocol.getState() == ProtocolState.LOGIN) {
            if (packet instanceof ServerboundHelloPacket helloPacket) {
                this.username = helloPacket.getUsername();

                if (session.getFlag(MinecraftConstants.VERIFY_USERS_KEY, true)) {
                    session.send(new ClientboundHelloPacket(SERVER_ID, KEY_PAIR.getPublic(), this.challenge));
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
                session.send(new ClientboundRegistryDataPacket(networkCodec));
                session.send(new ClientboundFinishConfigurationPacket());
            }
        } else if (protocol.getState() == ProtocolState.STATUS) {
            if (packet instanceof ServerboundStatusRequestPacket) {
                ServerInfoBuilder builder = session.getFlag(MinecraftConstants.SERVER_INFO_BUILDER_KEY);
                if (builder == null) {
                    builder = $ -> new ServerStatusInfo(
                            Component.text("A Minecraft Server"),
                            new PlayerInfo(0, 20, new ArrayList<>()),
                            new VersionInfo(protocol.getCodec().getMinecraftVersion(), protocol.getCodec().getProtocolVersion()),
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
            session.send(new ClientboundGameProfilePacket(session.getFlag(MinecraftConstants.PROFILE_KEY)));
        }
    }

    @Override
    public void disconnecting(DisconnectingEvent event) {
        MinecraftProtocol protocol = (MinecraftProtocol) event.session().getPacketProtocol();
        if (protocol.getState() == ProtocolState.LOGIN) {
            event.session().send(new ClientboundLoginDisconnectPacket(event.reason()));
        } else if (protocol.getState() == ProtocolState.GAME) {
            event.session().send(new ClientboundDisconnectPacket(event.reason()));
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
                    profile = sessionService.getProfileByServer(username, sessionService.getServerId(SERVER_ID, KEY_PAIR.getPublic(), this.key));
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
