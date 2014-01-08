package ch.spacebase.mc.protocol.packet.ingame.server.entity.spawn;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerSpawnPaintingPacket implements Packet {
	
	private int entityId;
	private Art art;
	private int x;
	private int y;
	private int z;
	private Direction direction;
	
	@SuppressWarnings("unused")
	private ServerSpawnPaintingPacket() {
	}
	
	public ServerSpawnPaintingPacket(int entityId, Art art, int x, int y, int z, Direction direction) {
		this.entityId = entityId;
		this.art = art;
		this.x = x;
		this.y = y;
		this.z = z;
		this.direction = direction;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public Art getArt() {
		return this.art;
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
	
	public Direction getDirection() {
		return this.direction;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readVarInt();
		this.art = Art.valueOf(in.readString());
		this.x = in.readInt();
		this.y = in.readInt();
		this.z = in.readInt();
		this.direction = Direction.values()[in.readInt()];
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.entityId);
		out.writeString(this.art.name());
		out.writeInt(this.x);
		out.writeInt(this.y);
		out.writeInt(this.z);
		out.writeInt(this.direction.ordinal());
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static enum Direction {
		BOTTOM,
		TOP,
		EAST,
		WEST,
		NORTH,
		SOUTH;
	}

	public static enum Art {
		Kebab,
		Aztec,
		Alban,
		Aztec2,
		Bomb,
		Plant,
		Wasteland,
		Pool,
		Courbet,
		Sea,
		Sunset,
		Creebet,
		Wanderer,
		Graham,
		Match,
		Bust,
		Stage,
		Void,
		SkullAndRoses,
		Wither,
		Fighters,
		Pointer,
		Pigscene,
		BurningSkull,
		Skeleton,
		DonkeyKong;
	}

}
