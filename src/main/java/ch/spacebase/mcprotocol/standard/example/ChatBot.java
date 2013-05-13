package ch.spacebase.mcprotocol.standard.example;

import java.text.DecimalFormat;

import ch.spacebase.mcprotocol.event.DisconnectEvent;
import ch.spacebase.mcprotocol.event.PacketRecieveEvent;
import ch.spacebase.mcprotocol.event.ProtocolListener;
import ch.spacebase.mcprotocol.exception.ConnectException;
import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.StandardProtocol;
import ch.spacebase.mcprotocol.standard.packet.PacketChat;
import ch.spacebase.mcprotocol.standard.packet.PacketPlayerPositionLook;

/**
 * A simple bot that prints
 * "Hello, this is Heisenberg at coordinate <coordinate>". Be sure to use the
 * Bukkit server setting online-mode=false in server.properties. Otherwise
 * supply a valid minecraft.net username and password.
 */
public class ChatBot {

	private Client client;
	private Listener listener;

	public ChatBot(String host, int port) {
		this.client = new Client(new StandardProtocol(), host, port);
		this.listener = new Listener();

		this.client.listen(this.listener);
	}

	public void login(String username) {
		this.client.setUser(username);

		try {
			this.client.connect();
		} catch (ConnectException e) {
			e.printStackTrace();
		}
	}

	public void say(String text) {
		PacketChat chat = new PacketChat();
		chat.message = text;
		this.client.send(chat);
	}

	public static void main(String[] args) {
		ChatBot bot = new ChatBot("127.0.0.1", 25565);
		System.out.println("Logging in...");
		bot.login("Heisenberg");
	}

	private class Listener extends ProtocolListener {
		@Override
		public void onPacketRecieve(PacketRecieveEvent event) {
			Packet packet = event.getPacket();

			switch(event.getPacket().getId()) {
			case 0x0D:
				onPositionLook((PacketPlayerPositionLook) packet);
				break;
			}
		}
		
		@Override
		public void onDisconnect(DisconnectEvent event) {
			System.out.println("Disconnected: " + event.getReason());
		}

		public void onPositionLook(PacketPlayerPositionLook packet) {
			client.send(packet);
			DecimalFormat format = new DecimalFormat("#.00");

			ChatBot.this.say("Hello, this is Heisenberg at coordinate (" + format.format(packet.x) + ", " + format.format(packet.y) + ", " + format.format(packet.z) + ")");
		}
	}

}
