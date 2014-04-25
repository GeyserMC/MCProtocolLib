package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.world.WorldBorderAction;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerWorldBorderPacket implements Packet {

	private WorldBorderAction action;

	private double radius;

	private double oldRadius;
	private double newRadius;
	private int speed;

	private double centerX;
	private double centerY;

	@SuppressWarnings("unused")
	private ServerWorldBorderPacket() {
	}

	public ServerWorldBorderPacket(double radius) {
		this.action = WorldBorderAction.SET_SIZE;
		this.radius = radius;
	}

	public ServerWorldBorderPacket(double oldRadius, double newRadius, int speed) {
		this.action = WorldBorderAction.LERP_SIZE;
		this.oldRadius = oldRadius;
		this.newRadius = newRadius;
		this.speed = speed;
	}

	public ServerWorldBorderPacket(double centerX, double centerY) {
		this.action = WorldBorderAction.SET_CENTER;
		this.centerX = centerX;
		this.centerY = centerY;
	}

	public WorldBorderAction getAction() {
		return this.action;
	}

	public double getRadius() {
		return this.radius;
	}

	public double getOldRadius() {
		return this.oldRadius;
	}

	public double getNewRadius() {
		return this.newRadius;
	}

	public int getSpeed() {
		return this.speed;
	}

	public double getCenterX() {
		return this.centerX;
	}

	public double getCenterY() {
		return this.centerY;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.action = MagicValues.key(WorldBorderAction.class, in.readVarInt());
		if(this.action == WorldBorderAction.SET_SIZE) {
			this.radius = in.readDouble();
		} else if(this.action == WorldBorderAction.LERP_SIZE) {
			this.oldRadius = in.readDouble();
			this.newRadius = in.readDouble();
			this.speed = in.readVarInt();
		} else if(this.action == WorldBorderAction.SET_CENTER) {
			this.centerX = in.readDouble();
			this.centerY = in.readDouble();
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(MagicValues.value(Integer.class, this.action));
		if(this.action == WorldBorderAction.SET_SIZE) {
			out.writeDouble(this.radius);
		} else if(this.action == WorldBorderAction.LERP_SIZE) {
			out.writeDouble(this.oldRadius);
			out.writeDouble(this.newRadius);
			out.writeVarInt(this.speed);
		} else if(this.action == WorldBorderAction.SET_CENTER) {
			out.writeDouble(this.centerX);
			out.writeDouble(this.centerX);
		}
	}

	@Override
	public boolean isPriority() {
		return false;
	}

}
