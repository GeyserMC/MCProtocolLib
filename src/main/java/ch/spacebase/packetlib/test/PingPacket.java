package ch.spacebase.packetlib.test;

import java.nio.ByteBuffer;

import ch.spacebase.packetlib.packet.Packet;

public class PingPacket implements Packet {

	private int id;
	
	public PingPacket() {
	}
	
	public PingPacket(int id) {
		this.id = id;
	}
	
	public int getPingId() {
		return this.id;
	}
	
	@Override
	public int getId() {
		return 0;
	}

	@Override
	public int getLength() {
		return 4;
	}

	@Override
	public void read(ByteBuffer in) {
		this.id = in.getInt();
	}

	@Override
	public void write(ByteBuffer out) {
		out.putInt(this.id);
	}

}
