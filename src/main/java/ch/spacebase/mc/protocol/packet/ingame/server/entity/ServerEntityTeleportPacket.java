package ch.spacebase.mc.protocol.packet.ingame.server.entity;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerEntityTeleportPacket implements Packet {
	
	protected int entityId;
	protected double x;
	protected double y;
	protected double z;
	protected float yaw;
	protected float pitch;
	
	@SuppressWarnings("unused")
	private ServerEntityTeleportPacket() {
	}
	
	public ServerEntityTeleportPacket(int entityId, double x, double y, double z, float yaw, float pitch) {
		this.entityId = entityId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public double getZ() {
		return this.z;
	}
	
	public float getYaw() {
		return this.yaw;
	}
	
	public float getPitch() {
		return this.pitch;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.x = in.readInt() / 32D;
		this.y = in.readInt() / 32D;
		this.z = in.readInt() / 32D;
		this.yaw = in.readByte() * 360 / 256f;
		this.pitch = in.readByte() * 360 / 256f;
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeInt((int) (this.x * 32));
		out.writeInt((int) (this.y * 32));
		out.writeInt((int) (this.z * 32));
		out.writeByte((byte) (this.yaw * 256 / 360));
		out.writeByte((byte) (this.pitch * 256 / 360));
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
