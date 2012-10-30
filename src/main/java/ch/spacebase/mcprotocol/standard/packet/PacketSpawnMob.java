package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.data.WatchableObject;
import ch.spacebase.mcprotocol.util.IOUtils;

public class PacketSpawnMob extends Packet {

	public int entityId;
	public byte type;
	public int x;
	public int y;
	public int z;
	public byte yaw;
	public byte pitch;
	public byte headYaw;
	public short velX;
	public short velY;
	public short velZ;
	public WatchableObject metadata[];
	
	public PacketSpawnMob() {
	}
	
	public PacketSpawnMob(int entityId, byte type, int x, int y, int z, byte yaw, byte pitch, byte headYaw, short velX, short velY, short velZ, WatchableObject metadata[]) {
		this.entityId = entityId;
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.headYaw = headYaw;
		this.velX = velX;
		this.velY = velY;
		this.velZ = velZ;
		this.metadata = metadata;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.entityId = in.readInt();
		this.type = in.readByte();
		this.x = in.readInt();
		this.y = in.readInt();
		this.z = in.readInt();
		this.yaw = in.readByte();
		this.pitch = in.readByte();
		this.headYaw = in.readByte();
		this.velX = in.readShort();
		this.velY = in.readShort();
		this.velZ = in.readShort();
		this.metadata = IOUtils.readMetadata(in);
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte(this.type);
		out.writeInt(this.x);
		out.writeInt(this.y);
		out.writeInt(this.z);
		out.writeByte(this.yaw);
		out.writeByte(this.pitch);
		out.writeByte(this.headYaw);
		out.writeShort(this.velX);
		out.writeShort(this.velY);
		out.writeShort(this.velZ);
		IOUtils.writeMetadata(out, this.metadata);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 24;
	}
	
}
