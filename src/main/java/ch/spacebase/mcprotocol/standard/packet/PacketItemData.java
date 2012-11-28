package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketItemData extends Packet {

	public short item;
	public short damage;
	public byte data[];

	public PacketItemData() {
	}

	public PacketItemData(short item, short damage, byte data[]) {
		this.item = item;
		this.damage = damage;
		this.data = data;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.item = in.readShort();
		this.damage = in.readShort();
		this.data = new byte[in.readUnsignedByte()];
		in.readFully(this.data);
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeShort(this.item);
		out.writeShort(this.damage);
		out.writeByte(this.data.length);
		out.write(data);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 131;
	}

}
