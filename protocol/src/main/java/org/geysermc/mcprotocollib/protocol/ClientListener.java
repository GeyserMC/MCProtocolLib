package org.geysermc.mcprotocollib.protocol;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.request.InvalidCredentialsException;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.exception.request.ServiceUnavailableException;
import com.github.steveice10.mc.auth.service.SessionService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
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

    @SneakyThrows
    @Override
    public void packetReceived(Session session, Packet packet) {
        MinecraftProtocol protocol = (MinecraftProtocol) session.getPacketProtocol();
        if (protocol.getState() == ProtocolState.LOGIN) {
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

                session.send(new ServerboundKeyPacket(helloPacket.getPublicKey(), key, helloPacket.getChallenge()));
                session.enableEncryption(protocol.enableEncryption(key));
            } else if (packet instanceof ClientboundGameProfilePacket) {
                session.send(new ServerboundLoginAcknowledgedPacket());
            } else if (packet instanceof ClientboundLoginDisconnectPacket loginDisconnectPacket) {
                session.disconnect(loginDisconnectPacket.getReason());
            } else if (packet instanceof ClientboundLoginCompressionPacket loginCompressionPacket) {
                session.setCompressionThreshold(loginCompressionPacket.getThreshold(), false);
            }
        } else if (protocol.getState() == ProtocolState.STATUS) {
            if (packet instanceof ClientboundStatusResponsePacket statusResponsePacket) {
                ServerStatusInfo info = statusResponsePacket.getInfo();
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

                session.disconnect("Finished");
            }
        } else if (protocol.getState() == ProtocolState.GAME) {
            if (packet instanceof ClientboundKeepAlivePacket keepAlivePacket && session.getFlag(MinecraftConstants.AUTOMATIC_KEEP_ALIVE_MANAGEMENT, true)) {
                session.send(new ServerboundKeepAlivePacket(keepAlivePacket.getPingId()));
            } else if (packet instanceof ClientboundDisconnectPacket disconnectPacket) {
                session.disconnect(disconnectPacket.getReason());
            } else if (packet instanceof ClientboundStartConfigurationPacket) {
                session.send(new ServerboundConfigurationAcknowledgedPacket());
            } else if (packet instanceof ClientboundTransferPacket transferPacket) {
                if (session.getFlag(MinecraftConstants.FOLLOW_TRANFERS, true)) {
                    TcpClientSession newSession = new TcpClientSession(transferPacket.getHost(), transferPacket.getPort(), session.getPacketProtocol());
                    newSession.setFlags(session.getFlags());
                    session.disconnect("Transferring");
                    newSession.connect(true, true);
                }
            }
        } else if (protocol.getState() == ProtocolState.CONFIGURATION) {
            if (packet instanceof ClientboundFinishConfigurationPacket) {
                session.send(new ServerboundFinishConfigurationPacket());
            } else if (packet instanceof ClientboundSelectKnownPacks) {
                if (session.getFlag(MinecraftConstants.SEND_BLANK_KNOWN_PACKS_RESPONSE, true)) {
                    session.send(new ServerboundSelectKnownPacks(Collections.emptyList()));
                }
            } else if (packet instanceof ClientboundTransferPacket transferPacket) {
                if (session.getFlag(MinecraftConstants.FOLLOW_TRANFERS, true)) {
                    TcpClientSession newSession = new TcpClientSession(transferPacket.getHost(), transferPacket.getPort(), session.getPacketProtocol());
                    newSession.setFlags(session.getFlags());
                    session.disconnect("Transferring");
                    newSession.connect(true, true);
                }
            }
        }
    }

    @Override
    public void packetSent(Session session, Packet packet) {
        MinecraftProtocol protocol = (MinecraftProtocol) session.getPacketProtocol();
        if (packet instanceof ClientIntentionPacket) {
            // Once the HandshakePacket has been sent, switch to the next protocol mode.
            protocol.setState(this.targetState);

            if (this.targetState == ProtocolState.LOGIN) {
                GameProfile profile = session.getFlag(MinecraftConstants.PROFILE_KEY);
                session.send(new ServerboundHelloPacket(profile.getName(), profile.getId()));
            } else {
                session.send(new ServerboundStatusRequestPacket());
            }
        } else if (packet instanceof ServerboundLoginAcknowledgedPacket) {
            protocol.setState(ProtocolState.CONFIGURATION); // LOGIN -> CONFIGURATION
        } else if (packet instanceof ServerboundFinishConfigurationPacket) {
            protocol.setState(ProtocolState.GAME); // CONFIGURATION -> GAME
        } else if (packet instanceof ServerboundConfigurationAcknowledgedPacket) {
            protocol.setState(ProtocolState.CONFIGURATION); // GAME -> CONFIGURATION
        }
    }

    @Override
    public void connected(ConnectedEvent event) {
        MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
        if (this.targetState == ProtocolState.LOGIN) {
            if (this.transferring) {
                event.getSession().send(new ClientIntentionPacket(protocol.getCodec().getProtocolVersion(), event.getSession().getHost(), event.getSession().getPort(), HandshakeIntent.TRANSFER));
            } else {
                event.getSession().send(new ClientIntentionPacket(protocol.getCodec().getProtocolVersion(), event.getSession().getHost(), event.getSession().getPort(), HandshakeIntent.LOGIN));
            }
        } else if (this.targetState == ProtocolState.STATUS) {
            event.getSession().send(new ClientIntentionPacket(protocol.getCodec().getProtocolVersion(), event.getSession().getHost(), event.getSession().getPort(), HandshakeIntent.STATUS));
        }
    }
}
