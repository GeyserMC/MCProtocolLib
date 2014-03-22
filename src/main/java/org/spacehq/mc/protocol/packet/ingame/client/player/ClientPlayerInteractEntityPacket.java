package org.spacehq.mc.protocol.packet.ingame.client.player;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ClientPlayerInteractEntityPacket implements Packet {
	
	private int entityId;
	private Action action;
	
	@SuppressWarnings("unused")
	private ClientPlayerInteractEntityPacket() {
	}
	
	public ClientPlayerInteractEntityPacket(int entityId, Action action) {
		this.entityId = entityId;
		this.action = action;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public Action getAction() {
		return this.action;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.action = Action.values()[in.readByte()];
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte(this.action.ordinal());
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static enum Action {
		INTERACT,
		ATTACK;
	}

}
