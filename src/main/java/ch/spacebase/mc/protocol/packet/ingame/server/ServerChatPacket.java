package ch.spacebase.mc.protocol.packet.ingame.server;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.message.Message;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerChatPacket implements Packet {
	
	private Message message;
	
	@SuppressWarnings("unused")
	private ServerChatPacket() {
	}
	
	public ServerChatPacket(String text) {
		this(Message.fromString(text));
	}
	
	public ServerChatPacket(Message message) {
		this.message = message;
	}
	
	public Message getMessage() {
		return this.message;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.message = Message.fromString(in.readString());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.message.toJsonString());
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
