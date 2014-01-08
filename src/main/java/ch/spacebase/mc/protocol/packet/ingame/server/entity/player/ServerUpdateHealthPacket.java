package ch.spacebase.mc.protocol.packet.ingame.server.entity.player;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerUpdateHealthPacket implements Packet {
	
	private float health;
	private int food;
	private float saturation;
	
	@SuppressWarnings("unused")
	private ServerUpdateHealthPacket() {
	}
	
	public ServerUpdateHealthPacket(float health, int food, float saturation) {
		this.health = health;
		this.food = food;
		this.saturation = saturation;
	}
	
	public float getHealth() {
		return this.health;
	}
	
	public int getFood() {
		return this.food;
	}
	
	public float getSaturation() {
		return this.saturation;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.health = in.readFloat();
		this.food = in.readShort();
		this.saturation = in.readFloat();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeFloat(this.health);
		out.writeShort(this.food);
		out.writeFloat(this.saturation);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
