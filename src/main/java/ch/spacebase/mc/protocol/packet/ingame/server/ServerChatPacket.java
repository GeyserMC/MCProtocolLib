package ch.spacebase.mc.protocol.packet.ingame.server;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.message.Message;
import ch.spacebase.mc.protocol.data.message.TextMessage;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerChatPacket implements Packet {
	
	private Message message;
	private MessageType type;
	
	@SuppressWarnings("unused")
	private ServerChatPacket() {
	}
	
	public ServerChatPacket(String text) {
		this(new TextMessage(text));
	}
	
	public ServerChatPacket(Message message) {
		this(message, MessageType.SYSTEM);
	}
	
	public ServerChatPacket(String text, MessageType type) {
		this(new TextMessage(text), type);
	}
	
	public ServerChatPacket(Message message, MessageType type) {
		this.message = message;
		this.type = type;
	}
	
	public Message getMessage() {
		return this.message;
	}
	
	public MessageType getType() {
		return this.type;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.message = Message.fromString(in.readString());
		this.type = MessageType.values()[in.readByte()];
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.message.toJsonString());
		out.writeByte(this.type.ordinal());
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static enum MessageType {
		CHAT,
		SYSTEM,
		NOTIFICATION;
	}

}
