package org.spacehq.mc.protocol.packet.status.client;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class StatusQueryPacket implements Packet {

	public StatusQueryPacket() {
	}

	public void read(NetInput in) throws IOException {
	}

	public void write(NetOutput out) throws IOException {
	}

	public boolean isPriority() {
		return false;
	}

}
