package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketOpenTileEditor extends Packet {

	public byte type;
	public int x;
	public int y;
	public int z;

	public PacketOpenTileEditor() {
	}

	public PacketOpenTileEditor(byte type, int x, int y, int z) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.type = in.readByte();
		this.x = in.readInt();
		this.y = in.readInt();
		this.z = in.readInt();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.type);
		out.writeInt(this.x);
		out.writeInt(this.y);
		out.writeInt(this.z);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 133;
	}

}
