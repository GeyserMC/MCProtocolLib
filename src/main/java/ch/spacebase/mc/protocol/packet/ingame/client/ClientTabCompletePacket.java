package ch.spacebase.mc.protocol.packet.ingame.client;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ClientTabCompletePacket implements Packet {
	
	private String text;
	
	@SuppressWarnings("unused")
	private ClientTabCompletePacket() {
	}
	
	public ClientTabCompletePacket(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.text = in.readString();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.text);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
