package ch.spacebase.mc.protocol.packet.status.client;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class StatusPingPacket implements Packet {
	
	private long time;
	
	@SuppressWarnings("unused")
	private StatusPingPacket() {
	}
	
	public StatusPingPacket(long time) {
		this.time = time;
	}
	
	public long getPingTime() {
		return this.time;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.time = in.readLong();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeLong(this.time);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
