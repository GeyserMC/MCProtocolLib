package ch.spacebase.mc.protocol.packet.ingame.client;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ClientChatPacket implements Packet {
	
	private String message;
	
	@SuppressWarnings("unused")
	private ClientChatPacket() {
	}
	
	public ClientChatPacket(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.message = in.readString();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.message);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
