package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.data.ItemStack;
import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

public class PacketSetSlot extends Packet {

	public byte id;
	public short slot;
	public ItemStack item;
	
	public PacketSetSlot() {
	}
	
	public PacketSetSlot(byte id, short slot, ItemStack item) {
		this.id = id;
		this.slot = slot;
		this.item = item;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.id = in.readByte();
		this.slot = in.readShort();
		this.item = new ItemStack();
		this.item.read(in);
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeByte(this.id);
		out.writeShort(this.slot);
		if(this.item != null) {
			this.item.write(out);
		}
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 103;
	}
	
}
