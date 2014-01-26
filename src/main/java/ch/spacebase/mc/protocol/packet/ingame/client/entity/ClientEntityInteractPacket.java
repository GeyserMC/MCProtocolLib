package ch.spacebase.mc.protocol.packet.ingame.client.entity;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.values.InteractAction;
import ch.spacebase.mc.protocol.data.game.values.MagicValues;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

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
