package ch.spacebase.packetlib.test;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class PingPacket implements Packet {

	private String id;
	
	@SuppressWarnings("unused")
	private PingPacket() {
	}
	
	public PingPacket(String id) {
		this.id = id;
	}
	
	public String getPingId() {
		return this.id;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.id = in.readString();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.id);
	}

	@Override
	public boolean isPriority() {
		return false;
	}

}
