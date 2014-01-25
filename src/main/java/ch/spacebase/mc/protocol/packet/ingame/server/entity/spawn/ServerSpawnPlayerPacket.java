package ch.spacebase.mc.protocol.packet.ingame.server.entity.spawn;

import java.io.IOException;

import ch.spacebase.mc.auth.GameProfile;
import ch.spacebase.mc.protocol.data.game.EntityMetadata;
import ch.spacebase.mc.util.NetUtil;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerSpawnPlayerPacket implements Packet {
	
	private int entityId;
	private GameProfile profile;
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	private int currentItem;
	private EntityMetadata metadata[];
	
	@SuppressWarnings("unused")
	private ServerSpawnPlayerPacket() {
	}
	
	public ServerSpawnPlayerPacket(int entityId, GameProfile profile, double x, double y, double z, float yaw, float pitch, int currentItem, EntityMetadata metadata[]) {
		this.entityId = entityId;
		this.profile = profile;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.currentItem = currentItem;
		this.metadata = metadata;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public GameProfile getProfile() {
		return this.profile;
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
	
	public int getCurrentItem() {
		return this.currentItem;
	}
	
	public EntityMetadata[] getMetadata() {
		return this.metadata;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readVarInt();
		this.profile = new GameProfile(in.readString(), in.readString());
		this.x = in.readInt() / 32D;
		this.y = in.readInt() / 32D;
		this.z = in.readInt() / 32D;
		this.yaw = in.readByte() * 360 / 256f;
		this.pitch = in.readByte() * 360 / 256f;
		this.currentItem = in.readShort();
		this.metadata = NetUtil.readEntityMetadata(in);
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.entityId);
		out.writeString(this.profile.getId());
		out.writeString(this.profile.getName());
		out.writeInt((int) (this.x * 32));
		out.writeInt((int) (this.y * 32));
		out.writeInt((int) (this.z * 32));
		out.writeByte((byte) (this.yaw * 256 / 360));
		out.writeByte((byte) (this.pitch * 256 / 360));
		out.writeShort(this.currentItem);
		NetUtil.writeEntityMetadata(out, this.metadata);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
