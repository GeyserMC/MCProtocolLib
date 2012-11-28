package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.data.ItemStack;

public class PacketSpawnDroppedItem extends Packet {

	public int entityId;
	public ItemStack item;
	public int x;
	public int y;
	public int z;
	public byte yaw;
	public byte pitch;
	public byte roll;

	public PacketSpawnDroppedItem() {
	}

	public PacketSpawnDroppedItem(int entityId, ItemStack item, int x, int y, int z, byte yaw, byte pitch, byte roll) {
		this.entityId = entityId;
		this.item = item;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.entityId = in.readInt();
		this.item = new ItemStack();
		this.item.read(in);
		this.x = in.readInt();
		this.y = in.readInt();
		this.z = in.readInt();
		this.yaw = in.readByte();
		this.pitch = in.readByte();
		this.roll = in.readByte();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.entityId);
		this.item.write(out);
		out.writeInt(this.x);
		out.writeInt(this.y);
		out.writeInt(this.z);
		out.writeByte(this.yaw);
		out.writeByte(this.pitch);
		out.writeByte(this.roll);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 21;
	}

}
