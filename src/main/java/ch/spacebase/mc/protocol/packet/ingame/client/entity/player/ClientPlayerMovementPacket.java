package ch.spacebase.mc.protocol.packet.ingame.client.entity.player;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ClientPlayerMovementPacket implements Packet {
	
	protected double x;
	protected double feetY;
	protected double headY;
	protected double z;
	protected float yaw;
	protected float pitch;
	protected boolean onGround;
	
	protected boolean pos = false;
	protected boolean rot = false;
	
	protected ClientPlayerMovementPacket() {
	}
	
	public ClientPlayerMovementPacket(boolean onGround) {
		this.onGround = onGround;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getFeetY() {
		return this.feetY;
	}
	
	public double getHeadY() {
		return this.headY;
	}
	
	public double getZ() {
		return this.z;
	}
	
	public double getYaw() {
		return this.yaw;
	}
	
	public double getPitch() {
		return this.pitch;
	}
	
	public boolean isOnGround() {
		return this.onGround;
	}

	@Override
	public void read(NetInput in) throws IOException {
		if(this.pos) {
			this.x = in.readDouble();
			this.feetY = in.readDouble();
			this.headY = in.readDouble();
			this.z = in.readDouble();
		}
		
		if(this.rot) {
			this.yaw = in.readFloat();
			this.pitch = in.readFloat();
		}
		
		this.onGround = in.readBoolean();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		if(this.pos) {
			out.writeDouble(this.x);
			out.writeDouble(this.feetY);
			out.writeDouble(this.headY);
			out.writeDouble(this.z);
		}
		
		if(this.rot) {
			out.writeFloat(this.yaw);
			out.writeFloat(this.pitch);
		}
		
		out.writeBoolean(this.onGround);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
