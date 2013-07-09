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

public class PacketPlayerBlockPlace extends Packet {

	public int x;
	public int y;
	public int z;
	public byte direction;
	public StandardItemStack item;
	public byte[] nbt;
	public byte cursorX;
	public byte cursorY;
	public byte cursorZ;

	public PacketPlayerBlockPlace() {
	}

	public PacketPlayerBlockPlace(int x, int y, int z, byte direction, StandardItemStack item, byte cursorX, byte cursorY, byte cursorZ) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.direction = direction;
		this.item = item;
		this.cursorX = cursorX;
		this.cursorY = cursorY;
		this.cursorZ = cursorZ;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.x = in.readInt();
		this.y = in.readUnsignedByte();
		this.z = in.readInt();
		this.direction = in.readByte();
		this.item = ((StandardInput) in).readItem();
		this.cursorX = in.readByte();
		this.cursorY = in.readByte();
		this.cursorZ = in.readByte();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.x);
		out.writeByte(this.y);
		out.writeInt(this.z);
		out.writeByte(this.direction);
		if(this.item != null) {
			((StandardOutput) out).writeItem(this.item);
		}
		
		out.writeByte(this.cursorX);
		out.writeByte(this.cursorY);
		out.writeByte(this.cursorZ);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 15;
	}

}
