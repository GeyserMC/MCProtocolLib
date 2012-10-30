package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.data.WatchableObject;
import ch.spacebase.mcprotocol.util.IOUtils;

public class PacketSpawnNamedEntity extends Packet {

	public int entityId;
	public String name;
	public int x;
	public int y;
	public int z;
	public byte yaw;
	public byte pitch;
	public short item;
	public WatchableObject metadata[];
	
	public PacketSpawnNamedEntity() {
	}
	
	public PacketSpawnNamedEntity(int entityId, String name, int x, int y, int z, byte yaw, byte pitch, short item, WatchableObject metadata[]) {
		this.entityId = entityId;
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.item = item;
		this.metadata = metadata;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.entityId = in.readInt();
		this.name = IOUtils.readString(in);
		this.x = in.readInt();
		this.y = in.readInt();
		this.z = in.readInt();
		this.yaw = in.readByte();
		this.pitch = in.readByte();
		this.item = in.readShort();
		this.metadata = IOUtils.readMetadata(in);
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.entityId);
		IOUtils.writeString(out, this.name);
		out.writeInt(this.x);
		out.writeInt(this.y);
		out.writeInt(this.z);
		out.writeByte(this.yaw);
		out.writeByte(this.pitch);
		out.writeShort(this.item);
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
		return 20;
	}
	
}
