package org.spacehq.mc.protocol.packet.ingame.server.entity.player;

import java.io.IOException;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

public class ServerChangeHeldItemPacket implements Packet {
	
	private int slot;
	
	@SuppressWarnings("unused")
	private ServerChangeHeldItemPacket() {
	}
	
	public ServerChangeHeldItemPacket(int slot) {
		this.slot = slot;
	}
	
	public int getSlot() {
		return this.slot;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.slot = in.readByte();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.slot);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
