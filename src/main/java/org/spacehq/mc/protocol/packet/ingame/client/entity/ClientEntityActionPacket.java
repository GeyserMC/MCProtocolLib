package org.spacehq.mc.protocol.packet.ingame.client.entity;

import java.io.IOException;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

public class ClientEntityActionPacket implements Packet {
	
	private int entityId;
	private Action action;
	private int jumpBoost;
	
	@SuppressWarnings("unused")
	private ClientEntityActionPacket() {
	}
	
	public ClientEntityActionPacket(int entityId, Action action) {
		this(entityId, action, 0);
	}
	
	public ClientEntityActionPacket(int entityId, Action action, int jumpBoost) {
		this.entityId = entityId;
		this.action = action;
		this.jumpBoost = jumpBoost;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public Action getAction() {
		return this.action;
	}
	
	public int getJumpBoost() {
		return this.jumpBoost;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.action = Action.values()[in.readByte() - 1];
		this.jumpBoost = in.readInt();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte(this.action.ordinal() + 1);
		out.writeInt(this.jumpBoost);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static enum Action {
		CROUCH,
		UNCROUCH,
		LEAVE_BED,
		START_SPRINTING,
		STOP_SPRINTING;
	}

}
