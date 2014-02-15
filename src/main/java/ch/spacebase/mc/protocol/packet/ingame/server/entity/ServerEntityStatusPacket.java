package ch.spacebase.mc.protocol.packet.ingame.server.entity;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.values.MagicValues;
import ch.spacebase.mc.protocol.data.game.values.entity.EntityStatus;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerEntityStatusPacket implements Packet {
	
	protected int entityId;
	protected EntityStatus status;
	
	@SuppressWarnings("unused")
	private ServerEntityStatusPacket() {
	}
	
	public ServerEntityStatusPacket(int entityId, EntityStatus status) {
		this.entityId = entityId;
		this.status = status;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public EntityStatus getStatus() {
		return this.status;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.status = MagicValues.key(EntityStatus.class, in.readByte());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte(MagicValues.value(Integer.class, this.status));
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
