package org.spacehq.mc.protocol.packet.ingame.server;

import java.io.IOException;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

public class ServerKeepAlivePacket implements Packet {
	
	private int id;
	
	@SuppressWarnings("unused")
	private ServerKeepAlivePacket() {
	}
	
	public ServerKeepAlivePacket(int id) {
		this.id = id;
	}
	
	public int getPingId() {
		return this.id;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.id = in.readInt();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.id);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
