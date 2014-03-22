package org.spacehq.mc.protocol;

import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.auth.SessionService;
import org.spacehq.mc.auth.exception.AuthenticationException;
import org.spacehq.mc.auth.exception.AuthenticationUnavailableException;
import org.spacehq.mc.auth.exception.InvalidCredentialsException;
import org.spacehq.mc.protocol.data.game.values.HandshakeIntent;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoHandler;
import org.spacehq.mc.protocol.data.status.handler.ServerPingTimeHandler;
import org.spacehq.mc.protocol.packet.handshake.client.HandshakePacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerDisconnectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import org.spacehq.mc.protocol.packet.login.client.EncryptionResponsePacket;
import org.spacehq.mc.protocol.packet.login.client.LoginStartPacket;
import org.spacehq.mc.protocol.packet.login.server.EncryptionRequestPacket;
import org.spacehq.mc.protocol.packet.login.server.LoginDisconnectPacket;
import org.spacehq.mc.protocol.packet.login.server.LoginSuccessPacket;
import org.spacehq.mc.protocol.packet.status.client.StatusPingPacket;
import org.spacehq.mc.protocol.packet.status.client.StatusQueryPacket;
import org.spacehq.mc.protocol.packet.status.server.StatusPongPacket;
import org.spacehq.mc.protocol.packet.status.server.StatusResponsePacket;
import org.spacehq.mc.util.CryptUtil;
import org.spacehq.packetlib.event.session.ConnectedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.PacketSentEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;

import javax.crypto.SecretKey;
import java.math.BigInteger;

public class ClientListener extends SessionAdapter {

	private SecretKey key;
	private String accessToken;

	public ClientListener(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public void packetReceived(PacketReceivedEvent event) {
		MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
		if(protocol.getMode() == ProtocolMode.LOGIN) {
			if(event.getPacket() instanceof EncryptionRequestPacket) {
				EncryptionRequestPacket packet = event.getPacket();
				this.key = CryptUtil.generateSharedKey();

				GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
				String serverHash = new BigInteger(CryptUtil.getServerIdHash(packet.getServerId(), packet.getPublicKey(), this.key)).toString(16);
				try {
					new SessionService().joinServer(profile, this.accessToken, serverHash);
				} catch(AuthenticationUnavailableException e) {
					event.getSession().disconnect("Login failed: Authentication service unavailable.");
					return;
				} catch(InvalidCredentialsException e) {
					event.getSession().disconnect("Login failed: Invalid login session.");
					return;
				} catch(AuthenticationException e) {
					event.getSession().disconnect("Login failed: Authentication error: " + e.getMessage());
					return;
				}

				event.getSession().send(new EncryptionResponsePacket(this.key, packet.getPublicKey(), packet.getVerifyToken()));
			} else if(event.getPacket() instanceof LoginSuccessPacket) {
				LoginSuccessPacket packet = event.getPacket();
				event.getSession().setFlag(ProtocolConstants.PROFILE_KEY, new GameProfile(packet.getPlayerId(), packet.getUsername()));
				protocol.setMode(ProtocolMode.GAME, true, event.getSession());
			} else if(event.getPacket() instanceof LoginDisconnectPacket) {
				LoginDisconnectPacket packet = event.getPacket();
				event.getSession().disconnect(packet.getReason().getFullText());
			}
		}

		if(protocol.getMode() == ProtocolMode.STATUS) {
			if(event.getPacket() instanceof StatusResponsePacket) {
				ServerStatusInfo info = event.<StatusResponsePacket>getPacket().getInfo();
				ServerInfoHandler handler = event.getSession().getFlag(ProtocolConstants.SERVER_INFO_HANDLER_KEY);
				if(handler != null) {
					handler.handle(event.getSession(), info);
				}

				event.getSession().send(new StatusPingPacket(System.nanoTime() / 1000000));
			} else if(event.getPacket() instanceof StatusPongPacket) {
				long time = System.nanoTime() / 1000000 - event.<StatusPongPacket>getPacket().getPingTime();
				ServerPingTimeHandler handler = event.getSession().getFlag(ProtocolConstants.SERVER_PING_TIME_HANDLER_KEY);
				if(handler != null) {
					handler.handle(event.getSession(), time);
				}

				event.getSession().disconnect("Finished");
			}
		}

		if(protocol.getMode() == ProtocolMode.GAME) {
			if(event.getPacket() instanceof ServerKeepAlivePacket) {
				event.getSession().send(new ClientKeepAlivePacket(event.<ServerKeepAlivePacket>getPacket().getPingId()));
			} else if(event.getPacket() instanceof ServerDisconnectPacket) {
				event.getSession().disconnect(event.<ServerDisconnectPacket>getPacket().getReason().getFullText());
			}
		}
	}

	@Override
	public void packetSent(PacketSentEvent event) {
		MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
		if(protocol.getMode() == ProtocolMode.LOGIN && event.getPacket() instanceof EncryptionResponsePacket) {
			protocol.enableEncryption(this.key);
		}
	}

	@Override
	public void connected(ConnectedEvent event) {
		MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
		if(protocol.getMode() == ProtocolMode.LOGIN) {
			GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
			protocol.setMode(ProtocolMode.HANDSHAKE, true, event.getSession());
			event.getSession().send(new HandshakePacket(ProtocolConstants.PROTOCOL_VERSION, event.getSession().getHost(), event.getSession().getPort(), HandshakeIntent.LOGIN));
			protocol.setMode(ProtocolMode.LOGIN, true, event.getSession());
			event.getSession().send(new LoginStartPacket(profile != null ? profile.getName() : ""));
		} else if(protocol.getMode() == ProtocolMode.STATUS) {
			protocol.setMode(ProtocolMode.HANDSHAKE, true, event.getSession());
			event.getSession().send(new HandshakePacket(ProtocolConstants.PROTOCOL_VERSION, event.getSession().getHost(), event.getSession().getPort(), HandshakeIntent.STATUS));
			protocol.setMode(ProtocolMode.STATUS, true, event.getSession());
			event.getSession().send(new StatusQueryPacket());
		}
	}

}
