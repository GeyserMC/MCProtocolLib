package com.github.steveice10.mc.protocol;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.request.InvalidCredentialsException;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.exception.request.ServiceUnavailableException;
import com.github.steveice10.mc.auth.service.SessionService;
import com.github.steveice10.mc.protocol.data.ProtocolState;
import com.github.steveice10.mc.protocol.data.UnexpectedEncryptionException;
import com.github.steveice10.mc.protocol.data.handshake.HandshakeIntent;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoHandler;
import com.github.steveice10.mc.protocol.data.status.handler.ServerPingTimeHandler;
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
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

/**
 * Handles making initial login and status requests for clients.
 */
@AllArgsConstructor
public class ClientListener extends SessionAdapter {
    private final @NonNull ProtocolState targetState;

    @Override
    public void packetReceived(Session session, Packet packet) {
        MinecraftProtocol protocol = (MinecraftProtocol) session.getPacketProtocol();
        if (protocol.getState() == ProtocolState.LOGIN) {
            if (packet instanceof ClientboundHelloPacket) {
                GameProfile profile = session.getFlag(MinecraftConstants.PROFILE_KEY);
                String accessToken = session.getFlag(MinecraftConstants.ACCESS_TOKEN_KEY);

                if (profile == null || accessToken == null) {
                    throw new UnexpectedEncryptionException();
                }

                ClientboundHelloPacket helloPacket = (ClientboundHelloPacket) packet;
                SecretKey key;
                try {
                    KeyGenerator gen = KeyGenerator.getInstance("AES");
                    gen.init(128);
                    key = gen.generateKey();
                } catch (NoSuchAlgorithmException e) {
                    throw new IllegalStateException("Failed to generate shared key.", e);
                }

                SessionService sessionService = session.getFlag(MinecraftConstants.SESSION_SERVICE_KEY, new SessionService());
                String serverId = sessionService.getServerId(helloPacket.getServerId(), helloPacket.getPublicKey(), key);
                try {
                    sessionService.joinServer(profile, accessToken, serverId);
                } catch (ServiceUnavailableException e) {
                    session.disconnect("Login failed: Authentication service unavailable.", e);
                    return;
                } catch (InvalidCredentialsException e) {
                    session.disconnect("Login failed: Invalid login session.", e);
                    return;
                } catch (RequestException e) {
                    session.disconnect("Login failed: Authentication error: " + e.getMessage(), e);
                    return;
                }

                session.send(new ServerboundKeyPacket(helloPacket.getPublicKey(), key, helloPacket.getVerifyToken()));
                session.enableEncryption(protocol.enableEncryption(key));
            } else if (packet instanceof ClientboundGameProfilePacket) {
                protocol.setState(ProtocolState.GAME);
            } else if (packet instanceof ClientboundLoginDisconnectPacket) {
                session.disconnect(((ClientboundLoginDisconnectPacket) packet).getReason().toString());
            } else if (packet instanceof ClientboundLoginCompressionPacket) {
                session.setCompressionThreshold(((ClientboundLoginCompressionPacket) packet).getThreshold(), false);
            }
        } else if (protocol.getState() == ProtocolState.STATUS) {
            if (packet instanceof ClientboundStatusResponsePacket) {
                ServerStatusInfo info = ((ClientboundStatusResponsePacket) packet).getInfo();
                ServerInfoHandler handler = session.getFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY);
                if (handler != null) {
                    handler.handle(session, info);
                }

                session.send(new ServerboundPingRequestPacket(System.currentTimeMillis()));
            } else if (packet instanceof ClientboundPongResponsePacket) {
                long time = System.currentTimeMillis() - ((ClientboundPongResponsePacket) packet).getPingTime();
                ServerPingTimeHandler handler = session.getFlag(MinecraftConstants.SERVER_PING_TIME_HANDLER_KEY);
                if (handler != null) {
                    handler.handle(session, time);
                }

                session.disconnect("Finished");
            }
        } else if (protocol.getState() == ProtocolState.GAME) {
            if (packet instanceof ClientboundKeepAlivePacket && session.getFlag(MinecraftConstants.AUTOMATIC_KEEP_ALIVE_MANAGEMENT, true)) {
                session.send(new ServerboundKeepAlivePacket(((ClientboundKeepAlivePacket) packet).getPingId()));
            } else if (packet instanceof ClientboundDisconnectPacket) {
                session.disconnect(((ClientboundDisconnectPacket) packet).getReason().toString());
            }
        }
    }

    @Override
    public void packetSent(Session session, Packet packet) {
        if (packet instanceof ClientIntentionPacket) {
            // Once the HandshakePacket has been sent, switch to the next protocol mode.
            MinecraftProtocol protocol = (MinecraftProtocol) session.getPacketProtocol();
            protocol.setState(this.targetState);

            if (this.targetState == ProtocolState.LOGIN) {
                GameProfile profile = session.getFlag(MinecraftConstants.PROFILE_KEY);
                session.send(new ServerboundHelloPacket(profile.getName()));
            } else {
                session.send(new ServerboundStatusRequestPacket());
            }
        }
    }

    @Override
    public void connected(ConnectedEvent event) {
        MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
        if (this.targetState == ProtocolState.LOGIN) {
            event.getSession().send(new ClientIntentionPacket(protocol.getCodec().getProtocolVersion(), event.getSession().getHost(), event.getSession().getPort(), HandshakeIntent.LOGIN));
        } else if (this.targetState == ProtocolState.STATUS) {
            event.getSession().send(new ClientIntentionPacket(protocol.getCodec().getProtocolVersion(), event.getSession().getHost(), event.getSession().getPort(), HandshakeIntent.STATUS));
        }
    }
}
