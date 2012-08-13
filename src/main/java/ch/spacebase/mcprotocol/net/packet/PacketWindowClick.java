package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.data.ItemStack;
import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

public class PacketWindowClick extends Packet {

	public byte id;
	public short slot;
	public byte rightclick;
	public short action;
	public boolean shift;
	public ItemStack clicked;
	
	public PacketWindowClick() {
	}
	
	public PacketWindowClick(byte id, short slot, byte rightclick, short action, boolean shift, ItemStack clicked) {
		this.id = id;
		this.slot = slot;
		this.rightclick = rightclick;
		this.action = action;
		this.shift = shift;
		this.clicked = clicked;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.id = in.readByte();
		this.slot = in.readShort();
		this.rightclick = in.readByte();
		this.action = in.readShort();
		this.shift = in.readBoolean();
		this.clicked = new ItemStack();
		this.clicked.read(in);
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeByte(this.id);
		out.writeShort(this.slot);
		out.writeByte(this.rightclick);
		out.writeShort(this.action);
		out.writeBoolean(this.shift);
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
		return 102;
	}
	
}
