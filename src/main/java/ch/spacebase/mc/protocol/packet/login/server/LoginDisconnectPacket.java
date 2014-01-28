package ch.spacebase.mc.protocol.packet.login.server;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.message.Message;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class LoginDisconnectPacket implements Packet {
	
	private Message message;
	
	@SuppressWarnings("unused")
	private LoginDisconnectPacket() {
	}
	
	public LoginDisconnectPacket(String text) {
		this(Message.fromString(text));
	}
	
	public LoginDisconnectPacket(Message message) {
		this.message = message;
	}
	
	public Message getReason() {
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
		return true;
	}

}
