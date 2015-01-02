package org.spacehq.mc.protocol.test;

import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.auth.exception.AuthenticationException;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.mc.protocol.ProtocolMode;
import org.spacehq.mc.protocol.ServerLoginHandler;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.data.game.values.setting.Difficulty;
import org.spacehq.mc.protocol.data.game.values.world.WorldType;
import org.spacehq.mc.protocol.data.message.*;
import org.spacehq.mc.protocol.data.status.PlayerInfo;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.mc.protocol.data.status.VersionInfo;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoBuilder;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoHandler;
import org.spacehq.mc.protocol.data.status.handler.ServerPingTimeHandler;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.event.server.ServerAdapter;
import org.spacehq.packetlib.event.server.SessionAddedEvent;
import org.spacehq.packetlib.event.server.SessionRemovedEvent;
import org.spacehq.packetlib.event.session.DisconnectedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import java.net.Proxy;
import java.util.Arrays;

public class Test {

	private static final boolean SPAWN_SERVER = true;
	private static final boolean VERIFY_USERS = false;
	private static final String HOST = "127.0.0.1";
	private static final int PORT = 25565;
	private static final Proxy PROXY = Proxy.NO_PROXY;
	private static final String USERNAME = "Username";
	private static final String PASSWORD = "Password";

	public static void main(String[] args) {
		if(SPAWN_SERVER) {
			Server server = new Server(HOST, PORT, MinecraftProtocol.class, new TcpSessionFactory(PROXY));
			server.setGlobalFlag(ProtocolConstants.VERIFY_USERS_KEY, VERIFY_USERS);
			server.setGlobalFlag(ProtocolConstants.SERVER_INFO_BUILDER_KEY, new ServerInfoBuilder() {
				@Override
				public ServerStatusInfo buildInfo(Session session) {
					return new ServerStatusInfo(new VersionInfo(ProtocolConstants.GAME_VERSION, ProtocolConstants.PROTOCOL_VERSION), new PlayerInfo(100, 0, new GameProfile[0]), new TextMessage("Hello world!"), null);
				}
			});

			server.setGlobalFlag(ProtocolConstants.SERVER_LOGIN_HANDLER_KEY, new ServerLoginHandler() {
				@Override
				public void loggedIn(Session session) {
					session.send(new ServerJoinGamePacket(0, false, GameMode.SURVIVAL, 0, Difficulty.PEACEFUL, 10, WorldType.DEFAULT, false));
				}
			});

			server.setGlobalFlag(ProtocolConstants.SERVER_COMPRESSION_THRESHOLD, 100);
			server.addListener(new ServerAdapter() {
				@Override
				public void sessionAdded(SessionAddedEvent event) {
					event.getSession().addListener(new SessionAdapter() {
						@Override
						public void packetReceived(PacketReceivedEvent event) {
							if(event.getPacket() instanceof ClientChatPacket) {
								ClientChatPacket packet = event.getPacket();
								GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
								System.out.println(profile.getName() + ": " + packet.getMessage());
								Message msg = new TextMessage("Hello, ").setStyle(new MessageStyle().setColor(ChatColor.GREEN));
								Message name = new TextMessage(profile.getName()).setStyle(new MessageStyle().setColor(ChatColor.AQUA).addFormat(ChatFormat.UNDERLINED));
								Message end = new TextMessage("!");
								msg.addExtra(name);
								msg.addExtra(end);
								event.getSession().send(new ServerChatPacket(msg));
							}
						}
					});
				}

				@Override
				public void sessionRemoved(SessionRemovedEvent event) {
					MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
					if(protocol.getMode() == ProtocolMode.GAME) {
						System.out.println("Closing server.");
						event.getServer().close();
					}
				}
			});

			server.bind();
		}

		status();
		login();
	}

	private static void status() {
		MinecraftProtocol protocol = new MinecraftProtocol(ProtocolMode.STATUS);
		Client client = new Client(HOST, PORT, protocol, new TcpSessionFactory(PROXY));
		client.getSession().setFlag(ProtocolConstants.SERVER_INFO_HANDLER_KEY, new ServerInfoHandler() {
			@Override
			public void handle(Session session, ServerStatusInfo info) {
				System.out.println("Version: " + info.getVersionInfo().getVersionName() + ", " + info.getVersionInfo().getProtocolVersion());
				System.out.println("Player Count: " + info.getPlayerInfo().getOnlinePlayers() + " / " + info.getPlayerInfo().getMaxPlayers());
				System.out.println("Players: " + Arrays.toString(info.getPlayerInfo().getPlayers()));
				System.out.println("Description: " + info.getDescription().getFullText());
				System.out.println("Icon: " + info.getIcon());
			}
		});

		client.getSession().setFlag(ProtocolConstants.SERVER_PING_TIME_HANDLER_KEY, new ServerPingTimeHandler() {
			@Override
			public void handle(Session session, long pingTime) {
				System.out.println("Server ping took " + pingTime + "ms");
			}
		});

		client.getSession().connect();
		while(client.getSession().isConnected()) {
			try {
				Thread.sleep(5);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void login() {
		MinecraftProtocol protocol = null;
		if(VERIFY_USERS) {
			try {
				protocol = new MinecraftProtocol(USERNAME, PASSWORD, false);
				System.out.println("Successfully authenticated user.");
			} catch(AuthenticationException e) {
				e.printStackTrace();
				return;
			}
		} else {
			protocol = new MinecraftProtocol(USERNAME);
		}

		Client client = new Client(HOST, PORT, protocol, new TcpSessionFactory(PROXY));
		client.getSession().addListener(new SessionAdapter() {
			@Override
			public void packetReceived(PacketReceivedEvent event) {
				if(event.getPacket() instanceof ServerJoinGamePacket) {
					event.getSession().send(new ClientChatPacket("Hello, this is a test of MCProtocolLib."));
				} else if(event.getPacket() instanceof ServerChatPacket) {
					Message message = event.<ServerChatPacket>getPacket().getMessage();
					System.out.println("Received Message: " + message.getFullText());
					if(message instanceof TranslationMessage) {
						System.out.println("Received Translation Components: " + Arrays.toString(((TranslationMessage) message).getTranslationParams()));
					}

					event.getSession().disconnect("Finished");
				}
			}

			@Override
			public void disconnected(DisconnectedEvent event) {
				System.out.println("Disconnected: " + Message.fromString(event.getReason()).getFullText());
			}
		});

		client.getSession().connect();
	}

}
