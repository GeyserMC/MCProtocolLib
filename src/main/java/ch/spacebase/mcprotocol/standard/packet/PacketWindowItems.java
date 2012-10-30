package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.data.ItemStack;

public class PacketWindowItems extends Packet {

	public byte id;
	public ItemStack items[];
	
	public PacketWindowItems() {
	}
	
	public PacketWindowItems(byte id, ItemStack items[]) {
		this.id = id;
		this.items = items;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.id = in.readByte();
		this.items = new ItemStack[in.readShort()];
		for(int count = 0; count < this.items.length; count++) {
			this.items[count] = new ItemStack();
			this.items[count].read(in);
		}
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeByte(this.id);
		out.writeShort(this.items.length);
		for(ItemStack item : this.items) {
			item.write(out);
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
		return 104;
	}
	
}
