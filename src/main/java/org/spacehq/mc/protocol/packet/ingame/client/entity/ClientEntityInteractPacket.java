package org.spacehq.mc.protocol.packet.ingame.client.entity;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.entity.player.InteractAction;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ClientEntityInteractPacket implements Packet {

	private int entityId;
	private InteractAction action;

	@SuppressWarnings("unused")
	private ClientEntityInteractPacket() {
	}

	public ClientEntityInteractPacket(int entityId, InteractAction action) {
		this.entityId = entityId;
		this.action = action;
	}

	public int getEntityId() {
		return this.entityId;
	}

	public InteractAction getAction() {
		return this.action;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readVarInt();
		this.action = MagicValues.key(InteractAction.class, in.readUnsignedByte());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.entityId);
		out.writeByte(MagicValues.value(Integer.class, this.action));
	}

	@Override
	public boolean isPriority() {
		return false;
	}

}
