package org.spacehq.mc.protocol.packet.status.client;

import java.io.IOException;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

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
