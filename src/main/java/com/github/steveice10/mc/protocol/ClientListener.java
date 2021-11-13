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
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.PacketSentEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
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
    public void packetReceived(PacketReceivedEvent event) {
        MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
        if (protocol.getState() == ProtocolState.LOGIN) {
            if (event.getPacket() instanceof ClientboundHelloPacket) {
                GameProfile profile = event.getSession().getFlag(MinecraftConstants.PROFILE_KEY);
                String accessToken = event.getSession().getFlag(MinecraftConstants.ACCESS_TOKEN_KEY);

                if (profile == null || accessToken == null) {
                    throw new UnexpectedEncryptionException();
                }

                ClientboundHelloPacket packet = event.getPacket();
                SecretKey key;
                try {
                    KeyGenerator gen = KeyGenerator.getInstance("AES");
                    gen.init(128);
                    key = gen.generateKey();
                } catch (NoSuchAlgorithmException e) {
                    throw new IllegalStateException("Failed to generate shared key.", e);
                }

                SessionService sessionService = event.getSession().getFlag(MinecraftConstants.SESSION_SERVICE_KEY, new SessionService());
                String serverId = sessionService.getServerId(packet.getServerId(), packet.getPublicKey(), key);
                try {
                    sessionService.joinServer(profile, accessToken, serverId);
                } catch (ServiceUnavailableException e) {
                    event.getSession().disconnect("Login failed: Authentication service unavailable.", e);
                    return;
                } catch (InvalidCredentialsException e) {
                    event.getSession().disconnect("Login failed: Invalid login session.", e);
                    return;
                } catch (RequestException e) {
                    event.getSession().disconnect("Login failed: Authentication error: " + e.getMessage(), e);
                    return;
                }

                event.getSession().send(new ServerboundKeyPacket(packet.getPublicKey(), key, packet.getVerifyToken()));
                protocol.enableEncryption(key);
            } else if (event.getPacket() instanceof ClientboundGameProfilePacket) {
                protocol.setState(ProtocolState.GAME);
            } else if (event.getPacket() instanceof ClientboundLoginDisconnectPacket) {
                event.getSession().disconnect(event.<ClientboundLoginDisconnectPacket>getPacket().getReason().toString());
            } else if (event.getPacket() instanceof ClientboundLoginCompressionPacket) {
                event.getSession().setCompressionThreshold(event.<ClientboundLoginCompressionPacket>getPacket().getThreshold());
            }
        } else if (protocol.getState() == ProtocolState.STATUS) {
            if (event.getPacket() instanceof ClientboundStatusResponsePacket) {
                ServerStatusInfo info = event.<ClientboundStatusResponsePacket>getPacket().getInfo();
                ServerInfoHandler handler = event.getSession().getFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY);
                if (handler != null) {
                    handler.handle(event.getSession(), info);
                }

                event.getSession().send(new ServerboundPingRequestPacket(System.currentTimeMillis()));
            } else if (event.getPacket() instanceof ClientboundPongResponsePacket) {
                long time = System.currentTimeMillis() - event.<ClientboundPongResponsePacket>getPacket().getPingTime();
                ServerPingTimeHandler handler = event.getSession().getFlag(MinecraftConstants.SERVER_PING_TIME_HANDLER_KEY);
                if (handler != null) {
                    handler.handle(event.getSession(), time);
                }

                event.getSession().disconnect("Finished");
            }
        } else if (protocol.getState() == ProtocolState.GAME) {
            if (event.getPacket() instanceof ClientboundKeepAlivePacket && event.getSession().getFlag(MinecraftConstants.AUTOMATIC_KEEP_ALIVE_MANAGEMENT, true)) {
                event.getSession().send(new ServerboundKeepAlivePacket(event.<ClientboundKeepAlivePacket>getPacket().getPingId()));
            } else if (event.getPacket() instanceof ClientboundDisconnectPacket) {
                event.getSession().disconnect(event.<ClientboundDisconnectPacket>getPacket().getReason().toString());
            }
        }
    }

    @Override
    public void packetSent(PacketSentEvent event) {
        if (event.getPacket() instanceof ClientIntentionPacket) {
            // Once the HandshakePacket has been sent, switch to the next protocol mode.
            MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
            protocol.setState(this.targetState);

            if (this.targetState == ProtocolState.LOGIN) {
                GameProfile profile = event.getSession().getFlag(MinecraftConstants.PROFILE_KEY);
                event.getSession().send(new ServerboundHelloPacket(profile.getName()));
            } else {
                event.getSession().send(new ServerboundStatusRequestPacket());
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
