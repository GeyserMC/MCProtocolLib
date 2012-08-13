package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.data.ItemStack;
import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

public class PacketCreativeSlot extends Packet {

	public short slot;
	public ItemStack clicked;
	
	public PacketCreativeSlot() {
	}
	
	public PacketCreativeSlot(short slot, ItemStack clicked) {
		this.slot = slot;
		this.clicked = clicked;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.slot = in.readShort();
		this.clicked = new ItemStack();
		this.clicked.read(in);
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeShort(this.slot);
		if(this.clicked != null) {
			this.clicked.write(out);
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
		return 107;
	}
	
}
