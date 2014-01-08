package ch.spacebase.mc.protocol.packet.ingame.server.entity;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerEntityMovementPacket implements Packet {
	
	protected int entityId;
	protected double moveX;
	protected double moveY;
	protected double moveZ;
	protected float yaw;
	protected float pitch;
	
	protected boolean pos = false;
	protected boolean rot = false;
	
	protected ServerEntityMovementPacket() {
	}
	
	public ServerEntityMovementPacket(int entityId) {
		this.entityId = entityId;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public double getMovementX() {
		return this.moveX;
	}
	
	public double getMovementY() {
		return this.moveY;
	}
	
	public double getMovementZ() {
		return this.moveZ;
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
		if(this.pos) {
			this.moveX = in.readByte() / 32D;
			this.moveY = in.readByte() / 32D;
			this.moveZ = in.readByte() / 32D;
		}
		
		if(this.rot) {
			this.yaw = in.readByte() * 360 / 256f;
			this.pitch = in.readByte() * 360 / 256f;
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		if(this.pos) {
			out.writeByte((int) (this.moveX * 32));
			out.writeByte((int) (this.moveY * 32));
			out.writeByte((int) (this.moveZ * 32));
		}
		
		if(this.rot) {
			out.writeByte((byte) (this.yaw * 256 / 360));
			out.writeByte((byte) (this.pitch * 256 / 360));
		}
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
