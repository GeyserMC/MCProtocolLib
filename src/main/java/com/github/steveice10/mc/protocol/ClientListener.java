package com.github.steveice10.mc.protocol;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.request.InvalidCredentialsException;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.exception.request.ServiceUnavailableException;
import com.github.steveice10.mc.auth.service.SessionService;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.data.UnexpectedEncryptionException;
import com.github.steveice10.mc.protocol.data.handshake.HandshakeIntent;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoHandler;
import com.github.steveice10.mc.protocol.data.status.handler.ServerPingTimeHandler;
import com.github.steveice10.mc.protocol.packet.handshake.client.HandshakePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDisconnectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.login.client.EncryptionResponsePacket;
import com.github.steveice10.mc.protocol.packet.login.client.LoginStartPacket;
import com.github.steveice10.mc.protocol.packet.login.server.EncryptionRequestPacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginDisconnectPacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginSetCompressionPacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginSuccessPacket;
import com.github.steveice10.mc.protocol.packet.status.client.StatusPingPacket;
import com.github.steveice10.mc.protocol.packet.status.client.StatusQueryPacket;
import com.github.steveice10.mc.protocol.packet.status.server.StatusPongPacket;
import com.github.steveice10.mc.protocol.packet.status.server.StatusResponsePacket;
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
    private final @NonNull SubProtocol targetSubProtocol;

    @Override
    public void packetReceived(PacketReceivedEvent event) {
        MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
        if (protocol.getSubProtocol() == SubProtocol.LOGIN) {
            if (event.getPacket() instanceof EncryptionRequestPacket) {
                GameProfile profile = event.getSession().getFlag(MinecraftConstants.PROFILE_KEY);
                String accessToken = event.getSession().getFlag(MinecraftConstants.ACCESS_TOKEN_KEY);

                if (profile == null || accessToken == null) {
                    throw new UnexpectedEncryptionException();
                }

                EncryptionRequestPacket packet = event.getPacket();
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

                event.getSession().send(new EncryptionResponsePacket(packet.getPublicKey(), key, packet.getVerifyToken()));
                protocol.enableEncryption(key);
            } else if (event.getPacket() instanceof LoginSuccessPacket) {
                protocol.setSubProtocol(SubProtocol.GAME, true, event.getSession());
            } else if (event.getPacket() instanceof LoginDisconnectPacket) {
                event.getSession().disconnect(event.<LoginDisconnectPacket>getPacket().getReason().toString());
            } else if (event.getPacket() instanceof LoginSetCompressionPacket) {
                event.getSession().setCompressionThreshold(event.<LoginSetCompressionPacket>getPacket().getThreshold());
            }
        } else if (protocol.getSubProtocol() == SubProtocol.STATUS) {
            if (event.getPacket() instanceof StatusResponsePacket) {
                ServerStatusInfo info = event.<StatusResponsePacket>getPacket().getInfo();
                ServerInfoHandler handler = event.getSession().getFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY);
                if (handler != null) {
                    handler.handle(event.getSession(), info);
                }

                event.getSession().send(new StatusPingPacket(System.currentTimeMillis()));
            } else if (event.getPacket() instanceof StatusPongPacket) {
                long time = System.currentTimeMillis() - event.<StatusPongPacket>getPacket().getPingTime();
                ServerPingTimeHandler handler = event.getSession().getFlag(MinecraftConstants.SERVER_PING_TIME_HANDLER_KEY);
                if (handler != null) {
                    handler.handle(event.getSession(), time);
                }

                event.getSession().disconnect("Finished");
            }
        } else if (protocol.getSubProtocol() == SubProtocol.GAME) {
            if (event.getPacket() instanceof ServerKeepAlivePacket && event.getSession().getFlag(MinecraftConstants.AUTOMATIC_KEEP_ALIVE_MANAGEMENT, true)) {
                event.getSession().send(new ClientKeepAlivePacket(event.<ServerKeepAlivePacket>getPacket().getPingId()));
            } else if (event.getPacket() instanceof ServerDisconnectPacket) {
                event.getSession().disconnect(event.<ServerDisconnectPacket>getPacket().getReason().toString());
            }
        }
    }

    @Override
    public void packetSent(PacketSentEvent event) {
        if (event.getPacket() instanceof HandshakePacket) {
            // Once the HandshakePacket has been sent, switch to the next protocol mode.
            MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
            protocol.setSubProtocol(this.targetSubProtocol, true, event.getSession());

            if (this.targetSubProtocol == SubProtocol.LOGIN) {
                GameProfile profile = event.getSession().getFlag(MinecraftConstants.PROFILE_KEY);
                event.getSession().send(new LoginStartPacket(profile.getName()));
            } else {
                event.getSession().send(new StatusQueryPacket());
            }
        }
    }

    @Override
    public void connected(ConnectedEvent event) {
        if (this.targetSubProtocol == SubProtocol.LOGIN) {
            event.getSession().send(new HandshakePacket(MinecraftConstants.PROTOCOL_VERSION, event.getSession().getHost(), event.getSession().getPort(), HandshakeIntent.LOGIN));
        } else if (this.targetSubProtocol == SubProtocol.STATUS) {
            event.getSession().send(new HandshakePacket(MinecraftConstants.PROTOCOL_VERSION, event.getSession().getHost(), event.getSession().getPort(), HandshakeIntent.STATUS));
        }
    }
}
