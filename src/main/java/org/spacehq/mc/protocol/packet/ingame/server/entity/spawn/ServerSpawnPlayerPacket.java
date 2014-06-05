package org.spacehq.mc.protocol.packet.ingame.server.entity.spawn;

import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.auth.properties.Property;
import org.spacehq.mc.protocol.data.game.EntityMetadata;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

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
		int numProperties = in.readVarInt();
		for(int count = 0; count < numProperties; count++) {
			String name = in.readString();
			String value = in.readString();
			String signature = in.readString();
			this.profile.getProperties().put(name, new Property(name, value, signature));
		}

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
		out.writeString(this.profile.getIdAsString());
		out.writeString(this.profile.getName());
		out.writeVarInt(this.profile.getProperties().size());
		for(Property property : this.profile.getProperties().values()) {
			out.writeString(property.getName());
			out.writeString(property.getValue());
			out.writeString(property.getSignature());
		}

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
