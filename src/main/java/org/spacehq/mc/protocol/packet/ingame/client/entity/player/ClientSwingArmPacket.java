package org.spacehq.mc.protocol.packet.ingame.client.entity.player;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ClientSwingArmPacket implements Packet {

	public ClientSwingArmPacket() {
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
