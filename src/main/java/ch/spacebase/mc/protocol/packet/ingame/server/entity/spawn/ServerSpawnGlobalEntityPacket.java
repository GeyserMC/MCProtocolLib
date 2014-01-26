package ch.spacebase.mc.protocol.packet.ingame.server.entity.spawn;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.values.GlobalEntityType;
import ch.spacebase.mc.protocol.data.game.values.MagicValues;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerSpawnGlobalEntityPacket implements Packet {

	private int entityId;
	private GlobalEntityType type;
	private int x;
	private int y;
	private int z;
	
	@SuppressWarnings("unused")
	private ServerSpawnGlobalEntityPacket() {
	}
	
	public ServerSpawnGlobalEntityPacket(int entityId, GlobalEntityType type, int x, int y, int z) {
		this.entityId = entityId;
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public GlobalEntityType getType() {
		return this.type;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getZ() {
		return this.z;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readVarInt();
		this.type = MagicValues.key(GlobalEntityType.class, in.readByte());
		this.x = in.readInt();
		this.y = in.readInt();
		this.z = in.readInt();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.entityId);
		out.writeByte(MagicValues.value(Integer.class, this.type));
		out.writeInt(this.x);
		out.writeInt(this.y);
		out.writeInt(this.z);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
}
