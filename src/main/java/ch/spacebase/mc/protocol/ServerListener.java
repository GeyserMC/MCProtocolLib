package ch.spacebase.mc.protocol;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.SecretKey;

import ch.spacebase.mc.auth.GameProfile;
import ch.spacebase.mc.auth.SessionService;
import ch.spacebase.mc.auth.exceptions.AuthenticationUnavailableException;
import ch.spacebase.mc.protocol.data.status.ServerStatusInfo;
import ch.spacebase.mc.protocol.data.status.handler.ServerInfoBuilder;
import ch.spacebase.mc.protocol.packet.handshake.client.HandshakePacket;
import ch.spacebase.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import ch.spacebase.mc.protocol.packet.ingame.server.ServerDisconnectPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import ch.spacebase.mc.protocol.packet.login.client.EncryptionResponsePacket;
import ch.spacebase.mc.protocol.packet.login.client.LoginStartPacket;
import ch.spacebase.mc.protocol.packet.login.server.EncryptionRequestPacket;
import ch.spacebase.mc.protocol.packet.login.server.LoginDisconnectPacket;
import ch.spacebase.mc.protocol.packet.login.server.LoginSuccessPacket;
import ch.spacebase.mc.protocol.packet.status.client.StatusPingPacket;
import ch.spacebase.mc.protocol.packet.status.client.StatusQueryPacket;
import ch.spacebase.mc.protocol.packet.status.server.StatusPongPacket;
import ch.spacebase.mc.protocol.packet.status.server.StatusResponsePacket;
import ch.spacebase.mc.util.CryptUtil;
import ch.spacebase.packetlib.Session;
import ch.spacebase.packetlib.event.session.DisconnectingEvent;
import ch.spacebase.packetlib.event.session.PacketReceivedEvent;
import ch.spacebase.packetlib.event.session.SessionAdapter;

public class ServerListener extends SessionAdapter {

	private static KeyPair pair = CryptUtil.generateKeyPair();
	
	private byte verifyToken[] = new byte[4];
	private String serverId = "";
	private String username = "";
	
	private long lastPingTime = 0;
	private int lastPingId = 0;
	
	public ServerListener() {
		new Random().nextBytes(this.verifyToken);
	}
	
	@Override
	public void packetReceived(PacketReceivedEvent event) {
		MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
		if(protocol.getMode() == ProtocolMode.HANDSHAKE) {
			if(event.getPacket() instanceof HandshakePacket) {
				HandshakePacket packet = event.getPacket();
				switch(packet.getIntent()) {
					case 1:
						protocol.setMode(ProtocolMode.STATUS, false, event.getSession());
						break;
					case 2:
						protocol.setMode(ProtocolMode.LOGIN, false, event.getSession());
						if(packet.getProtocolVersion() > ProtocolConstants.PROTOCOL_VERSION) {
							event.getSession().disconnect("Outdated server! I'm still on " + ProtocolConstants.GAME_VERSION + ".");
						} else if(packet.getProtocolVersion() < ProtocolConstants.PROTOCOL_VERSION) {
							event.getSession().disconnect("Outdated client! Please use " + ProtocolConstants.GAME_VERSION + ".");
						}
						
						break;
					default:
						throw new UnsupportedOperationException("Invalid client intent: " + packet.getIntent());
				}
			}
		}
		
		if(protocol.getMode() == ProtocolMode.LOGIN) {
			if(event.getPacket() instanceof LoginStartPacket) {
				this.username = event.<LoginStartPacket>getPacket().getUsername();
				boolean verify = event.getSession().getFlag(ProtocolConstants.VERIFY_USERS_KEY);
				if(verify) {
					event.getSession().send(new EncryptionRequestPacket(this.serverId, pair.getPublic(), this.verifyToken));
				} else {
					event.getSession().send(new LoginSuccessPacket("", this.username));
					event.getSession().setFlag(ProtocolConstants.PROFILE_KEY, new GameProfile("", this.username));
					protocol.setMode(ProtocolMode.GAME, false, event.getSession());
					new KeepAliveThread(event.getSession()).start();
					ServerLoginHandler handler = event.getSession().getFlag(ProtocolConstants.SERVER_LOGIN_HANDLER_KEY);
					if(handler != null) {
						handler.loggedIn(event.getSession());
					}
				}
			} else if(event.getPacket() instanceof EncryptionResponsePacket) {
				EncryptionResponsePacket packet = event.getPacket();
				PrivateKey privateKey = pair.getPrivate();
				if(!Arrays.equals(this.verifyToken, packet.getVerifyToken(privateKey))) {
					throw new IllegalStateException("Invalid nonce!");
				} else {
					SecretKey key = packet.getSecretKey(privateKey);
					protocol.enableEncryption(key);
					new UserAuthThread(event.getSession(), key).start();
				}
			}
		}
		
		if(protocol.getMode() == ProtocolMode.STATUS) {
			if(event.getPacket() instanceof StatusQueryPacket) {
				ServerInfoBuilder builder = event.getSession().getFlag(ProtocolConstants.SERVER_INFO_BUILDER_KEY);
				if(builder == null) {
					event.getSession().disconnect("No server info builder set.");
				}
				
				ServerStatusInfo info = builder.buildInfo();
				event.getSession().send(new StatusResponsePacket(info));
			} else if(event.getPacket() instanceof StatusPingPacket) {
				event.getSession().send(new StatusPongPacket(event.<StatusPingPacket>getPacket().getPingTime()));
			}
		}
		
		if(protocol.getMode() == ProtocolMode.GAME) {
			if(event.getPacket() instanceof ClientKeepAlivePacket) {
				ClientKeepAlivePacket packet = event.getPacket();
				if(packet.getPingId() == this.lastPingId) {
					long time = (System.nanoTime() / 1000000L) - this.lastPingTime;
					event.getSession().setFlag(ProtocolConstants.PING_KEY, time);
				}
			}
		}
	}
	
	@Override
	public void disconnecting(DisconnectingEvent event) {
		MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
		if(protocol.getMode() == ProtocolMode.LOGIN) {
			event.getSession().send(new LoginDisconnectPacket(event.getReason()));
		} else if(protocol.getMode() == ProtocolMode.GAME) {
			event.getSession().send(new ServerDisconnectPacket(event.getReason()));
		}
	}
	
	private class UserAuthThread extends Thread {
		private Session session;
		private SecretKey key;
		
		public UserAuthThread(Session session, SecretKey key) {
			this.key = key;
			this.session = session;
		}
		
		@Override
		public void run() {
			MinecraftProtocol protocol = (MinecraftProtocol) this.session.getPacketProtocol();
			try {
				String serverHash = new BigInteger(CryptUtil.getServerIdHash(serverId, pair.getPublic(), this.key)).toString(16);
				SessionService service = new SessionService();
				GameProfile profile = service.hasJoinedServer(new GameProfile(null, username), serverHash);
				if(profile != null) {
					this.session.send(new LoginSuccessPacket(profile.getId(), profile.getName()));
					this.session.setFlag(ProtocolConstants.PROFILE_KEY, profile);
					protocol.setMode(ProtocolMode.GAME, false, this.session);
					new KeepAliveThread(this.session).start();
					ServerLoginHandler handler = this.session.getFlag(ProtocolConstants.SERVER_LOGIN_HANDLER_KEY);
					if(handler != null) {
						handler.loggedIn(this.session);
					}
				} else {
					this.session.disconnect("Failed to verify username!");
				}
			} catch(AuthenticationUnavailableException e) {
				this.session.disconnect("Authentication servers are down. Please try again later, sorry!");
			}
		}
	}
	
	private class KeepAliveThread extends Thread {
		private Session session;
		
		public KeepAliveThread(Session session) {
			this.session = session;
		}
		
		@Override
		public void run() {
			while(this.session.isConnected()) {
				long curr = System.nanoTime() / 1000000L;
				long time = curr - lastPingTime;
				if(time > 2000) {
					lastPingTime = curr;
					lastPingId = (int) curr;
					this.session.send(new ServerKeepAlivePacket(lastPingId));
				}
				
				try {
					Thread.sleep(10);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
