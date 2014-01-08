package ch.spacebase.mc.protocol.packet.ingame.client.entity.player;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ClientSteerVehiclePacket implements Packet {
	
	private float sideways;
	private float forward;
	private boolean jump;
	private boolean dismount;
	
	@SuppressWarnings("unused")
	private ClientSteerVehiclePacket() {
	}
	
	public ClientSteerVehiclePacket(float sideways, float forward, boolean jump, boolean dismount) {
		this.sideways = sideways;
		this.forward = forward;
		this.jump = jump;
		this.dismount = dismount;
	}
	
	public float getSideways() {
		return this.sideways;
	}
	
	public float getForward() {
		return this.forward;
	}
	
	public boolean getJumping() {
		return this.jump;
	}
	
	public boolean getDismounting() {
		return this.dismount;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.sideways = in.readFloat();
		this.forward = in.readFloat();
		this.jump = in.readBoolean();
		this.dismount = in.readBoolean();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeFloat(this.sideways);
		out.writeFloat(this.forward);
		out.writeBoolean(this.jump);
		out.writeBoolean(this.dismount);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
