package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.data.StandardItemStack;
import ch.spacebase.mcprotocol.standard.io.StandardInput;
import ch.spacebase.mcprotocol.standard.io.StandardOutput;

public class PacketWindowClick extends Packet {

	public byte id;
	public short slot;
	public byte mousebutton;
	public short action;
	public boolean shift;
	public StandardItemStack clicked;

	public PacketWindowClick() {
	}

	public PacketWindowClick(byte id, short slot, byte mousebutton, short action, boolean shift, StandardItemStack clicked) {
		this.id = id;
		this.slot = slot;
		this.mousebutton = mousebutton;
		this.action = action;
		this.shift = shift;
		this.clicked = clicked;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.id = in.readByte();
		this.slot = in.readShort();
		this.mousebutton = in.readByte();
		this.action = in.readShort();
		this.shift = in.readBoolean();
		this.clicked = ((StandardInput) in).readItem();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.id);
		out.writeShort(this.slot);
		out.writeByte(this.mousebutton);
		out.writeShort(this.action);
		out.writeBoolean(this.shift);
		if(this.clicked != null) {
			((StandardOutput) out).writeItem(this.clicked);
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
