package ch.spacebase.mc.protocol.packet.ingame.server.entity.spawn;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.Position;
import ch.spacebase.mc.util.NetUtil;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerSpawnPaintingPacket implements Packet {
	
	private int entityId;
	private Art art;
	private Position position;
	private Direction direction;
	
	@SuppressWarnings("unused")
	private ServerSpawnPaintingPacket() {
	}
	
	public ServerSpawnPaintingPacket(int entityId, Art art, Position position, Direction direction) {
		this.entityId = entityId;
		this.art = art;
		this.position = position;
		this.direction = direction;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public Art getArt() {
		return this.art;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public Direction getDirection() {
		return this.direction;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readVarInt();
		this.art = Art.valueOf(in.readString());
		this.position = NetUtil.readPosition(in);
		this.direction = Direction.values()[in.readUnsignedByte()];
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.entityId);
		out.writeString(this.art.name());
		NetUtil.writePosition(out, this.position);
		out.writeByte(this.direction.ordinal());
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
