package ch.spacebase.mc.protocol.packet.ingame.server.entity.player;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerPlayerPositionRotationPacket implements Packet {
	
	protected double x;
	protected double y;
	protected double z;
	protected float yaw;
	protected float pitch;
	protected boolean onGround;
	
	@SuppressWarnings("unused")
	private ServerPlayerPositionRotationPacket() {
	}
	
	public ServerPlayerPositionRotationPacket(double x, double y, double z, float yaw, float pitch, boolean onGround) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
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
	
	public boolean isOnGround() {
		return this.onGround;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.x = in.readDouble();
		this.y = in.readDouble();
		this.z = in.readDouble();
		this.yaw = in.readFloat();
		this.pitch = in.readFloat();
		this.onGround = in.readBoolean();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeDouble(this.x);
		out.writeDouble(this.y);
		out.writeDouble(this.z);
		out.writeFloat(this.yaw);
		out.writeFloat(this.pitch);
		out.writeBoolean(this.onGround);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
