package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.data.ItemStack;

public class PacketEntityEquipment extends Packet {

	public int entityId;
	public short slot;
	public ItemStack item;
	
	public PacketEntityEquipment() {
	}
	
	public PacketEntityEquipment(int entityId, short slot, ItemStack item) {
		this.entityId = entityId;
		this.slot = slot;
		this.item = item;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public short getSlot() {
		return this.slot;
	}
	
	public ItemStack getItem() {
		return this.item;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.entityId = in.readInt();
		this.slot = in.readShort();
		this.item = new ItemStack();
		this.item.read(in);
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.entityId);
		out.writeShort(this.slot);
		this.item.write(out);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 5;
	}
	
}
