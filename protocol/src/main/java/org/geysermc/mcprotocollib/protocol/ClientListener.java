package org.geysermc.mcprotocollib.protocol;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.request.InvalidCredentialsException;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.exception.request.ServiceUnavailableException;
import com.github.steveice10.mc.auth.service.SessionService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.ConnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.network.tcp.TcpClientSession;
import org.geysermc.mcprotocollib.protocol.data.ProtocolState;
import org.geysermc.mcprotocollib.protocol.data.UnexpectedEncryptionException;
import org.geysermc.mcprotocollib.protocol.data.handshake.HandshakeIntent;
import org.geysermc.mcprotocollib.protocol.data.status.ServerStatusInfo;
import org.geysermc.mcprotocollib.protocol.data.status.handler.ServerInfoHandler;
import org.geysermc.mcprotocollib.protocol.data.status.handler.ServerPingTimeHandler;
import org.geysermc.mcprotocollib.protocol.packet.common.clientbound.ClientboundDisconnectPacket;
import org.geysermc.mcprotocollib.protocol.packet.common.clientbound.ClientboundKeepAlivePacket;
import org.geysermc.mcprotocollib.protocol.packet.common.clientbound.ClientboundTransferPacket;
import org.geysermc.mcprotocollib.protocol.packet.common.serverbound.ServerboundKeepAlivePacket;
import org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound.ClientboundFinishConfigurationPacket;
import org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound.ClientboundSelectKnownPacks;
import org.geysermc.mcprotocollib.protocol.packet.configuration.serverbound.ServerboundFinishConfigurationPacket;
import org.geysermc.mcprotocollib.protocol.packet.configuration.serverbound.ServerboundSelectKnownPacks;
import org.geysermc.mcprotocollib.protocol.packet.handshake.serverbound.ClientIntentionPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundStartConfigurationPacket;
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

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

/**
 * Handles making initial login and status requests for clients.
 */
@AllArgsConstructor
public class ClientListener extends SessionAdapter {
    private final @NonNull ProtocolState targetState;
    private final boolean transferring;

    @Override
    public void packetReceived(Session session, Packet packet) {
        MinecraftProtocol protocol = (MinecraftProtocol) session.getPacketProtocol();
        if (protocol.getInboundState() == ProtocolState.LOGIN) {
            if (packet instanceof ClientboundHelloPacket helloPacket) {
                GameProfile profile = session.getFlag(MinecraftConstants.PROFILE_KEY);
                String accessToken = session.getFlag(MinecraftConstants.ACCESS_TOKEN_KEY);

                if (profile == null || accessToken == null) {
                    throw new UnexpectedEncryptionException();
                }

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

                // TODO: Add disabled multiplayer and banned from playing online errors
                try {
                    sessionService.joinServer(profile, accessToken, serverId);
                } catch (ServiceUnavailableException e) {
                    session.disconnect(Component.translatable("disconnect.loginFailedInfo", Component.translatable("disconnect.loginFailedInfo.serversUnavailable")), e);
                    return;
                } catch (InvalidCredentialsException e) {
                    session.disconnect(Component.translatable("disconnect.loginFailedInfo", Component.translatable("disconnect.loginFailedInfo.invalidSession")), e);
                    return;
                } catch (RequestException e) {
                    session.disconnect(Component.translatable("disconnect.loginFailedInfo", e.getMessage()), e);
                    return;
                }

                session.send(new ServerboundKeyPacket(helloPacket.getPublicKey(), key, helloPacket.getChallenge()),
                    () -> session.enableEncryption(protocol.enableEncryption(key)));
            } else if (packet instanceof ClientboundGameProfilePacket) {
                session.switchInboundProtocol(() -> protocol.setInboundState(ProtocolState.CONFIGURATION));
                session.send(new ServerboundLoginAcknowledgedPacket());
                session.switchOutboundProtocol(() -> protocol.setOutboundState(ProtocolState.CONFIGURATION));

                // Send client brand here
                // Send client information here
            } else if (packet instanceof ClientboundLoginDisconnectPacket loginDisconnectPacket) {
                session.disconnect(loginDisconnectPacket.getReason());
            } else if (packet instanceof ClientboundLoginCompressionPacket loginCompressionPacket) {
                session.setCompressionThreshold(loginCompressionPacket.getThreshold(), false);
            }
        } else if (protocol.getInboundState() == ProtocolState.STATUS) {
            if (packet instanceof ClientboundStatusResponsePacket statusResponsePacket) {
                ServerStatusInfo info = statusResponsePacket.parseInfo();
                ServerInfoHandler handler = session.getFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY);
                if (handler != null) {
                    handler.handle(session, info);
                }

                session.send(new ServerboundPingRequestPacket(System.currentTimeMillis()));
            } else if (packet instanceof ClientboundPongResponsePacket pongResponsePacket) {
                long time = System.currentTimeMillis() - pongResponsePacket.getPingTime();
                ServerPingTimeHandler handler = session.getFlag(MinecraftConstants.SERVER_PING_TIME_HANDLER_KEY);
                if (handler != null) {
                    handler.handle(session, time);
                }

                session.disconnect(Component.translatable("multiplayer.status.finished"));
            }
        } else if (protocol.getInboundState() == ProtocolState.GAME) {
            if (packet instanceof ClientboundKeepAlivePacket keepAlivePacket && session.getFlag(MinecraftConstants.AUTOMATIC_KEEP_ALIVE_MANAGEMENT, true)) {
                session.send(new ServerboundKeepAlivePacket(keepAlivePacket.getPingId()));
            } else if (packet instanceof ClientboundDisconnectPacket disconnectPacket) {
                session.disconnect(disconnectPacket.getReason());
            } else if (packet instanceof ClientboundStartConfigurationPacket) {
                session.switchInboundProtocol(() -> protocol.setInboundState(ProtocolState.CONFIGURATION));
                session.send(new ServerboundConfigurationAcknowledgedPacket());
                session.switchOutboundProtocol(() -> protocol.setOutboundState(ProtocolState.CONFIGURATION));
            } else if (packet instanceof ClientboundTransferPacket transferPacket) {
                if (session.getFlag(MinecraftConstants.FOLLOW_TRANSFERS, true)) {
                    TcpClientSession newSession = new TcpClientSession(transferPacket.getHost(), transferPacket.getPort(), session.getPacketProtocol());
                    newSession.setFlags(session.getFlags());
                    session.disconnect(Component.translatable("disconnect.transfer"));
                    newSession.connect(true, true);
                }
            }
        } else if (protocol.getInboundState() == ProtocolState.CONFIGURATION) {
            if (packet instanceof ClientboundFinishConfigurationPacket) {
                session.switchInboundProtocol(() -> protocol.setInboundState(ProtocolState.GAME));
                session.send(new ServerboundFinishConfigurationPacket());
                session.switchOutboundProtocol(() -> protocol.setOutboundState(ProtocolState.GAME));
            } else if (packet instanceof ClientboundSelectKnownPacks) {
                if (session.getFlag(MinecraftConstants.SEND_BLANK_KNOWN_PACKS_RESPONSE, true)) {
                    session.send(new ServerboundSelectKnownPacks(Collections.emptyList()));
                }
            } else if (packet instanceof ClientboundTransferPacket transferPacket) {
                if (session.getFlag(MinecraftConstants.FOLLOW_TRANSFERS, true)) {
                    TcpClientSession newSession = new TcpClientSession(transferPacket.getHost(), transferPacket.getPort(), session.getPacketProtocol());
                    newSession.setFlags(session.getFlags());
                    session.disconnect(Component.translatable("disconnect.transfer"));
                    newSession.connect(true, true);
                }
            }
        }
    }

    @Override
    public void connected(ConnectedEvent event) {
        Session session = event.getSession();
        MinecraftProtocol protocol = (MinecraftProtocol) session.getPacketProtocol();
        ClientIntentionPacket intention = new ClientIntentionPacket(protocol.getCodec().getProtocolVersion(), event.getSession().getHost(), event.getSession().getPort(), switch (this.targetState) {
            case LOGIN -> transferring ? HandshakeIntent.TRANSFER : HandshakeIntent.LOGIN;
            case STATUS -> HandshakeIntent.STATUS;
            default -> throw new IllegalStateException("Unexpected value: " + this.targetState);
        });

        switch (this.targetState) {
            case LOGIN -> {
                session.switchInboundProtocol(() -> protocol.setInboundState(ProtocolState.LOGIN));
                session.send(intention);
                session.switchOutboundProtocol(() -> protocol.setOutboundState(ProtocolState.LOGIN));
                GameProfile profile = session.getFlag(MinecraftConstants.PROFILE_KEY);
                session.send(new ServerboundHelloPacket(profile.getName(), profile.getId()));
            }
            case STATUS -> {
                session.switchInboundProtocol(() -> protocol.setInboundState(ProtocolState.STATUS));
                session.send(intention);
                session.switchOutboundProtocol(() -> protocol.setOutboundState(ProtocolState.STATUS));
                session.send(new ServerboundStatusRequestPacket());
            }
        }
    }
}
