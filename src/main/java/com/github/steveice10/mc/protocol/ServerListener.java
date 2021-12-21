package com.github.steveice10.mc.protocol;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.service.SessionService;
import com.github.steveice10.mc.protocol.data.ProtocolState;
import com.github.steveice10.mc.protocol.data.status.PlayerInfo;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.VersionInfo;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoBuilder;
import com.github.steveice10.mc.protocol.packet.handshake.serverbound.ClientIntentionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundDisconnectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.login.clientbound.ClientboundGameProfilePacket;
import com.github.steveice10.mc.protocol.packet.login.clientbound.ClientboundHelloPacket;
import com.github.steveice10.mc.protocol.packet.login.clientbound.ClientboundLoginCompressionPacket;
import com.github.steveice10.mc.protocol.packet.login.clientbound.ClientboundLoginDisconnectPacket;
import com.github.steveice10.mc.protocol.packet.login.serverbound.ServerboundHelloPacket;
import com.github.steveice10.mc.protocol.packet.login.serverbound.ServerboundKeyPacket;
import com.github.steveice10.mc.protocol.packet.status.clientbound.ClientboundPongResponsePacket;
import com.github.steveice10.mc.protocol.packet.status.clientbound.ClientboundStatusResponsePacket;
import com.github.steveice10.mc.protocol.packet.status.serverbound.ServerboundPingRequestPacket;
import com.github.steveice10.mc.protocol.packet.status.serverbound.ServerboundStatusRequestPacket;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectingEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import net.kyori.adventure.text.Component;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Arrays;
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

    private byte[] verifyToken = new byte[4];
    private String username = "";

    private long lastPingTime = 0;
    private int lastPingId = 0;

    public ServerListener() {
        new Random().nextBytes(this.verifyToken);
    }

    @Override
    public void connected(ConnectedEvent event) {
        event.getSession().setFlag(MinecraftConstants.PING_KEY, 0);
    }

    @Override
    public void packetReceived(Session session, Packet packet) {
        MinecraftProtocol protocol = (MinecraftProtocol) session.getPacketProtocol();
        if (protocol.getState() == ProtocolState.HANDSHAKE) {
            if (packet instanceof ClientIntentionPacket) {
                ClientIntentionPacket intentionPacket = (ClientIntentionPacket) packet;
                switch (intentionPacket.getIntent()) {
                    case STATUS:
                        protocol.setState(ProtocolState.STATUS);
                        break;
                    case LOGIN:
                        protocol.setState(ProtocolState.LOGIN);
                        if (intentionPacket.getProtocolVersion() > protocol.getCodec().getProtocolVersion()) {
                            session.disconnect("Outdated server! I'm still on " + protocol.getCodec().getMinecraftVersion() + ".");
                        } else if (intentionPacket.getProtocolVersion() < protocol.getCodec().getProtocolVersion()) {
                            session.disconnect("Outdated client! Please use " + protocol.getCodec().getMinecraftVersion() + ".");
                        }

                        break;
                    default:
                        throw new UnsupportedOperationException("Invalid client intent: " + intentionPacket.getIntent());
                }
            }
        }

        if (protocol.getState() == ProtocolState.LOGIN) {
            if (packet instanceof ServerboundHelloPacket) {
                this.username = ((ServerboundHelloPacket) packet).getUsername();

                if (session.getFlag(MinecraftConstants.VERIFY_USERS_KEY, true)) {
                    session.send(new ClientboundHelloPacket(SERVER_ID, KEY_PAIR.getPublic(), this.verifyToken));
                } else {
                    new Thread(new UserAuthTask(session, null)).start();
                }
            } else if (packet instanceof ServerboundKeyPacket) {
                ServerboundKeyPacket keyPacket = (ServerboundKeyPacket) packet;
                PrivateKey privateKey = KEY_PAIR.getPrivate();
                if (!Arrays.equals(this.verifyToken, keyPacket.getVerifyToken(privateKey))) {
                    session.disconnect("Invalid nonce!");
                    return;
                }

                SecretKey key = keyPacket.getSecretKey(privateKey);
                session.enableEncryption(protocol.enableEncryption(key));
                new Thread(new UserAuthTask(session, key)).start();
            }
        }

        if (protocol.getState() == ProtocolState.STATUS) {
            if (packet instanceof ServerboundStatusRequestPacket) {
                ServerInfoBuilder builder = session.getFlag(MinecraftConstants.SERVER_INFO_BUILDER_KEY);
                if (builder == null) {
                    builder = $ -> new ServerStatusInfo(
                            new VersionInfo(protocol.getCodec().getMinecraftVersion(), protocol.getCodec().getProtocolVersion()),
                            new PlayerInfo(0, 20, new GameProfile[0]),
                            Component.text("A Minecraft Server"),
                            null
                    );
                }

                ServerStatusInfo info = builder.buildInfo(session);
                session.send(new ClientboundStatusResponsePacket(info));
            } else if (packet instanceof ServerboundPingRequestPacket) {
                session.send(new ClientboundPongResponsePacket(((ServerboundPingRequestPacket) packet).getPingTime()));
            }
        }

        if (protocol.getState() == ProtocolState.GAME) {
            if (packet instanceof ServerboundKeepAlivePacket) {
                if (((ServerboundKeepAlivePacket) packet).getPingId() == this.lastPingId) {
                    long time = System.currentTimeMillis() - this.lastPingTime;
                    session.setFlag(MinecraftConstants.PING_KEY, time);
                }
            }
        }
    }

    @Override
    public void packetSent(Session session, Packet packet) {
        if (packet instanceof ClientboundLoginCompressionPacket) {
            session.setCompressionThreshold(((ClientboundLoginCompressionPacket) packet).getThreshold(), true);
            session.send(new ClientboundGameProfilePacket((GameProfile) session.getFlag(MinecraftConstants.PROFILE_KEY)));
        } else if (packet instanceof ClientboundGameProfilePacket) {
            ((MinecraftProtocol) session.getPacketProtocol()).setState(ProtocolState.GAME);
            ServerLoginHandler handler = session.getFlag(MinecraftConstants.SERVER_LOGIN_HANDLER_KEY);
            if (handler != null) {
                handler.loggedIn(session);
            }

            if (session.getFlag(MinecraftConstants.AUTOMATIC_KEEP_ALIVE_MANAGEMENT, true)) {
                new Thread(new KeepAliveTask(session)).start();
            }
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

    private class UserAuthTask implements Runnable {
        private Session session;
        private SecretKey key;

        public UserAuthTask(Session session, SecretKey key) {
            this.key = key;
            this.session = session;
        }

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

    private class KeepAliveTask implements Runnable {
        private Session session;

        public KeepAliveTask(Session session) {
            this.session = session;
        }

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
