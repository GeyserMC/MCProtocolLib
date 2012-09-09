package ch.spacebase.mcprotocol.example;

import java.text.DecimalFormat;

import ch.spacebase.mcprotocol.event.LoginEvent;
import ch.spacebase.mcprotocol.event.LoginFinishEvent;
import ch.spacebase.mcprotocol.event.PacketRecieveEvent;
import ch.spacebase.mcprotocol.event.ProtocolListener;
import ch.spacebase.mcprotocol.exception.ConnectException;
import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.packet.Packet;
import ch.spacebase.mcprotocol.net.packet.PacketChat;
import ch.spacebase.mcprotocol.net.packet.PacketPlayerPositionLook;

/**
 * A simple bot that prints "Hello, this is Heisenberg at coordinate <coordinate>".
 * Be sure to use the Bukkit server setting online-mode=false in server.properties.
 * Otherwise supply a valid minecraft.net username and password.
 */
public class ChatBot {
	private Client client;
	private Listener listener;
	
	public ChatBot(String host, int port) {
		client = new Client(host, port);
		listener = new Listener();
		
		client.listen(listener);
	}
	
	public void login(String username) {
		client.setUser(username);
		
		try {
			client.connect();
		} catch (ConnectException e) {
			e.printStackTrace();
		}
	}
	
	public void say(String text) {
		PacketChat chat = new PacketChat();
		
		chat.message = text;
		
		client.send(chat);
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
			
			switch (event.getPacket().getId()) {
			case 0x0D:
				onPositionLook((PacketPlayerPositionLook) packet);
				break;
			}
		}

		@Override
		public void onLogin(LoginEvent event) {
			// no-op
		}

		@Override
		public void onLoginFinish(LoginFinishEvent event) {
			// no-op
		}
		
		public void onPositionLook(PacketPlayerPositionLook packet) {
			client.send(packet);
			
			DecimalFormat format = new DecimalFormat("#.00");
			
			ChatBot.this.say("Hello, this is Heisenberg at coordinate (" +
			format.format(packet.x) + ", " +  format.format(packet.y) + ", " + format.format(packet.z) +
			")");
		}
	}
}
