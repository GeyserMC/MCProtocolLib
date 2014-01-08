package ch.spacebase.mc.protocol.packet.login.server;

import java.io.IOException;

import ch.spacebase.mc.util.message.Message;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class LoginDisconnectPacket implements Packet {
	
	private Message message;
	
	@SuppressWarnings("unused")
	private LoginDisconnectPacket() {
	}
	
	public LoginDisconnectPacket(String message) {
		this(new Message(message));
	}
	
	public LoginDisconnectPacket(Message message) {
		this.message = message;
	}
	
	public String getRawReason() {
		return this.getReason().getRawText();
	}
	
	public Message getReason() {
		return this.message;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.message = new Message(in.readString(), true);
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.message.toString());
	}
	
	@Override
	public boolean isPriority() {
		return true;
	}

}
