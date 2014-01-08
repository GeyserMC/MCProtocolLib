package ch.spacebase.mc.protocol.packet.ingame.server.entity.spawn;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerSpawnObjectPacket implements Packet {
	
	private int entityId;
	private Type type;
	private double x;
	private double y;
	private double z;
	private float pitch;
	private float yaw;
	private int data;
	private double motX;
	private double motY;
	private double motZ;
	
	@SuppressWarnings("unused")
	private ServerSpawnObjectPacket() {
	}
	
	public ServerSpawnObjectPacket(int entityId, Type type, double x, double y, double z, float yaw, float pitch) {
		this(entityId, type, 0, x, y, z, yaw, pitch, 0, 0, 0);
	}
	
	public ServerSpawnObjectPacket(int entityId, Type type, int data, double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ) {
		this.entityId = entityId;
		this.type = type;
		this.data = data;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.motX = motX;
		this.motY = motY;
		this.motZ = motZ;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public int getData() {
		return this.data;
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
	
	public double getMotionX() {
		return this.motX;
	}
	
	public double getMotionY() {
		return this.motY;
	}
	
	public double getMotionZ() {
		return this.motZ;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readVarInt();
		this.type = idToType(in.readByte());
		this.x = in.readInt() / 32D;
		this.y = in.readInt() / 32D;
		this.z = in.readInt() / 32D;
		this.pitch = in.readByte() * 360 / 256f;
		this.yaw = in.readByte() * 360 / 256f;
		this.data = in.readInt();
		if(this.data > 0) {
			this.motX = in.readShort() / 8000D;
			this.motY = in.readShort() / 8000D;
			this.motZ = in.readShort() / 8000D;
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.entityId);
		out.writeByte(typeToId(this.type));
		out.writeInt((int) (this.x * 32));
		out.writeInt((int) (this.y * 32));
		out.writeInt((int) (this.z * 32));
		out.writeByte((byte) (this.pitch * 256 / 360));
		out.writeByte((byte) (this.yaw * 256 / 360));
		out.writeInt(this.data);
		if(this.data > 0) {
			out.writeShort((int) (this.motX * 8000));
			out.writeShort((int) (this.motY * 8000));
			out.writeShort((int) (this.motZ * 8000));
		}
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	private static Type idToType(byte id) throws IOException {
		switch(id) {
			case 1:
				return Type.BOAT;
			case 2:
				return Type.ITEM;
			case 10:
				return Type.MINECART;
			case 50:
				return Type.PRIMED_TNT;
			case 51:
				return Type.ENDER_CRYSTAL;
			case 60:
				return Type.ARROW;
			case 61:
				return Type.SNOWBALL;
			case 62:
				return Type.EGG;
			case 63:
				return Type.GHAST_FIREBALL;
			case 64:
				return Type.BLAZE_FIREBALL;
			case 65:
				return Type.ENDER_PEARL;
			case 66:
				return Type.WITHER_HEAD_PROJECTILE;
			case 70:
				return Type.FALLING_BLOCK;
			case 71:
				return Type.ITEM_FRAME;
			case 72:
				return Type.EYE_OF_ENDER;
			case 73:
				return Type.POTION;
			case 75:
				return Type.EXP_BOTTLE;
			case 76:
				return Type.FIREWORK_ROCKET;
			case 77:
				return Type.LEASH_KNOT;
			case 90:
				return Type.FISH_HOOK;
			default:
				throw new IOException("Unknown object type id: " + id);
		}
	}
	
	private static byte typeToId(Type type) throws IOException {
		switch(type) {
			case BOAT:
				return 1;
			case ITEM:
				return 2;
			case MINECART:
				return 10;
			case PRIMED_TNT:
				return 50;
			case ENDER_CRYSTAL:
				return 51;
			case ARROW:
				return 60;
			case SNOWBALL:
				return 61;
			case EGG:
				return 62;
			case GHAST_FIREBALL:
				return 63;
			case BLAZE_FIREBALL:
				return 64;
			case ENDER_PEARL:
				return 65;
			case WITHER_HEAD_PROJECTILE:
				return 66;
			case FALLING_BLOCK:
				return 70;
			case ITEM_FRAME:
				return 71;
			case EYE_OF_ENDER:
				return 72;
			case POTION:
				return 73;
			case EXP_BOTTLE:
				return 75;
			case FIREWORK_ROCKET:
				return 76;
			case LEASH_KNOT:
				return 77;
			case FISH_HOOK:
				return 90;
			default:
				throw new IOException("Unmapped object type: " + type);
		}
	}
	
	public static enum Type {
		BOAT,
		ITEM,
		MINECART,
		PRIMED_TNT,
		ENDER_CRYSTAL,
		ARROW,
		SNOWBALL,
		EGG,
		GHAST_FIREBALL,
		BLAZE_FIREBALL,
		ENDER_PEARL,
		WITHER_HEAD_PROJECTILE,
		FALLING_BLOCK,
		ITEM_FRAME,
		EYE_OF_ENDER,
		POTION,
		EXP_BOTTLE,
		FIREWORK_ROCKET,
		LEASH_KNOT,
		FISH_HOOK;
	}
	
	public static class MinecartType {
		public static final int NORMAL = 0;
		public static final int CHEST = 1;
		public static final int POWERED = 2;
		public static final int TNT = 3;
		public static final int MOB_SPAWNER = 4;
		public static final int HOPPER = 5;
		public static final int COMMAND_BLOCK = 6;
	}
	
	public static class ItemFrameDirection {
		public static final int SOUTH = 0;
		public static final int WEST = 1;
		public static final int NORTH = 2;
		public static final int EAST = 3;
	}
	
	public static class FallingBlockData {
		public static final int BLOCK_TYPE_TO_DATA(FallingBlockType type) {
			return BLOCK_TYPE_TO_DATA(type.getId(), type.getMetadata());
		}
		
		public static final int BLOCK_TYPE_TO_DATA(int block, int metadata) {
			return block | metadata << 16;
		}
		
		public static final FallingBlockType DATA_TO_BLOCK_TYPE(int data) {
			return new FallingBlockType(data & 65535, data >> 16);
		}
	}

	public static class FallingBlockType {
		private int id;
		private int metadata;
		
		public FallingBlockType(int id, int metadata) {
			this.id = id;
			this.metadata = metadata;
		}
		
		public int getId() {
			return this.id;
		}
		
		public int getMetadata() {
			return this.metadata;
		}
	}
	
}
