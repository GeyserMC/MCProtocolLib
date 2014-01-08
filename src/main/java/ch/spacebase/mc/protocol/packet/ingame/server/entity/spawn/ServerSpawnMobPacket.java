package ch.spacebase.mc.protocol.packet.ingame.server.entity.spawn;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.EntityMetadata;
import ch.spacebase.mc.util.NetUtil;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerSpawnMobPacket implements Packet {
	
	private int entityId;
	private Type type;
	private double x;
	private double y;
	private double z;
	private float pitch;
	private float yaw;
	private float headYaw;
	private double motX;
	private double motY;
	private double motZ;
	private EntityMetadata metadata[];
	
	@SuppressWarnings("unused")
	private ServerSpawnMobPacket() {
	}
	
	public ServerSpawnMobPacket(int entityId, Type type, double x, double y, double z, float yaw, float pitch, float headYaw, double motX, double motY, double motZ, EntityMetadata metadata[]) {
		this.entityId = entityId;
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.headYaw = headYaw;
		this.motX = motX;
		this.motY = motY;
		this.motZ = motZ;
		this.metadata = metadata;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public double getZ() {
		return this.z;
	}
	
	public float getYaw() {
		return this.yaw;
	}
	
	public float getPitch() {
		return this.pitch;
	}
	
	public float getHeadYaw() {
		return this.headYaw;
	}
	
	public double getMotionX() {
		return this.motX;
	}
	
	public double getMotionY() {
		return this.motY;
	}
	
	public double getMotionZ() {
		return this.motZ;
	}
	
	public EntityMetadata[] getMetadata() {
		return this.metadata;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readVarInt();
		this.type = idToType(in.readByte());
		this.x = in.readInt() / 32D;
		this.y = in.readInt() / 32D;
		this.z = in.readInt() / 32D;
		this.yaw = in.readByte() * 360 / 256f;
		this.pitch = in.readByte() * 360 / 256f;
		this.headYaw = in.readByte() * 360 / 256f;
		this.motX = in.readShort() / 8000D;
		this.motY = in.readShort() / 8000D;
		this.motZ = in.readShort() / 8000D;
		this.metadata = NetUtil.readEntityMetadata(in);
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.entityId);
		out.writeByte(typeToId(this.type));
		out.writeInt((int) (this.x * 32));
		out.writeInt((int) (this.y * 32));
		out.writeInt((int) (this.z * 32));
		out.writeByte((byte) (this.yaw * 256 / 360));
		out.writeByte((byte) (this.pitch * 256 / 360));
		out.writeByte((byte) (this.headYaw * 256 / 360));
		out.writeShort((int) (this.motX * 8000));
		out.writeShort((int) (this.motY * 8000));
		out.writeShort((int) (this.motZ * 8000));
		NetUtil.writeEntityMetadata(out, this.metadata);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	private static Type idToType(byte id) throws IOException {
		switch(id) {
			case 50:
				return Type.CREEPER;
			case 51:
				return Type.SKELETON;
			case 52:
				return Type.SPIDER;
			case 53:
				return Type.GIANT_ZOMBIE;
			case 54:
				return Type.ZOMBIE;
			case 55:
				return Type.SLIME;
			case 56:
				return Type.GHAST;
			case 57:
				return Type.ZOMBIE_PIGMAN;
			case 58:
				return Type.ENDERMAN;
			case 59:
				return Type.CAVE_SPIDER;
			case 60:
				return Type.SILVERFISH;
			case 61:
				return Type.BLAZE;
			case 62:
				return Type.MAGMA_CUBE;
			case 63:
				return Type.ENDER_DRAGON;
			case 64:
				return Type.WITHER;
			case 65:
				return Type.BAT;
			case 66:
				return Type.WITCH;
			case 90:
				return Type.PIG;
			case 91:
				return Type.SHEEP;
			case 92:
				return Type.COW;
			case 93:
				return Type.CHICKEN;
			case 94:
				return Type.SQUID;
			case 95:
				return Type.WOLF;
			case 96:
				return Type.MOOSHROOM;
			case 97:
				return Type.SNOWMAN;
			case 98:
				return Type.OCELOT;
			case 99:
				return Type.IRON_GOLEM;
			case 100:
				return Type.HORSE;
			case 120:
				return Type.VILLAGER;
			default:
				throw new IOException("Unknown mob type id: " + id);
		}
	}
	
	private static byte typeToId(Type type) throws IOException {
		switch(type) {
			case CREEPER:
				return 50;
			case SKELETON:
				return 51;
			case SPIDER:
				return 52;
			case GIANT_ZOMBIE:
				return 53;
			case ZOMBIE:
				return 54;
			case SLIME:
				return 55;
			case GHAST:
				return 56;
			case ZOMBIE_PIGMAN:
				return 57;
			case ENDERMAN:
				return 58;
			case CAVE_SPIDER:
				return 59;
			case SILVERFISH:
				return 60;
			case BLAZE:
				return 61;
			case MAGMA_CUBE:
				return 62;
			case ENDER_DRAGON:
				return 63;
			case WITHER:
				return 64;
			case BAT:
				return 65;
			case WITCH:
				return 66;
			case PIG:
				return 90;
			case SHEEP:
				return 91;
			case COW:
				return 92;
			case CHICKEN:
				return 93;
			case SQUID:
				return 94;
			case WOLF:
				return 95;
			case MOOSHROOM:
				return 96;
			case SNOWMAN:
				return 97;
			case OCELOT:
				return 98;
			case IRON_GOLEM:
				return 99;
			case HORSE:
				return 100;
			case VILLAGER:
				return 120;
			default:
				throw new IOException("Unmapped mob type: " + type);
		}
	}
	
	public static enum Type {
		CREEPER,
		SKELETON,
		SPIDER,
		GIANT_ZOMBIE,
		ZOMBIE,
		SLIME,
		GHAST,
		ZOMBIE_PIGMAN,
		ENDERMAN,
		CAVE_SPIDER,
		SILVERFISH,
		BLAZE,
		MAGMA_CUBE,
		ENDER_DRAGON,
		WITHER,
		BAT,
		WITCH,
		PIG,
		SHEEP,
		COW,
		CHICKEN,
		SQUID,
		WOLF,
		MOOSHROOM,
		SNOWMAN,
		OCELOT,
		IRON_GOLEM,
		HORSE,
		VILLAGER;
	}

}
