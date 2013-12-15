package ch.spacebase.mc.protocol.packet.status.client;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class StatusQueryPacket implements Packet {
	
	public StatusQueryPacket() {
	}

	@Override
	public void read(NetInput in) throws IOException {
	}

	@Override
	public void write(NetOutput out) throws IOException {
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
}
