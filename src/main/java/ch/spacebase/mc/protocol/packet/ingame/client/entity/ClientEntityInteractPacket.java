package ch.spacebase.mc.protocol.packet.ingame.client.entity;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ClientEntityInteractPacket implements Packet {
	
	private int entityId;
	private Action action;
	
	@SuppressWarnings("unused")
	private ClientEntityInteractPacket() {
	}
	
	public ClientEntityInteractPacket(int entityId, Action action) {
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
