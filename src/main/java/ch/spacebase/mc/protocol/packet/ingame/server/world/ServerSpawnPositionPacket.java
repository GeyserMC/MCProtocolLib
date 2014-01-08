package ch.spacebase.mc.protocol.packet.ingame.server.world;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerSpawnPositionPacket implements Packet {
	
	private int x;
	private int y;
	private int z;
	
	@SuppressWarnings("unused")
	private ServerSpawnPositionPacket() {
	}
	
	public ServerSpawnPositionPacket(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getZ() {
		return this.z;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.x = in.readInt();
		this.y = in.readInt();
		this.z = in.readInt();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.x);
		out.writeInt(this.y);
		out.writeInt(this.z);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
