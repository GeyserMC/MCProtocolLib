package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketPlayerDigging extends Packet {

	public byte status;
	public int x;
	public int y;
	public int z;
	public byte face;

	public PacketPlayerDigging() {
	}

	public PacketPlayerDigging(byte status, int x, int y, int z, byte face) {
		this.status = status;
		this.x = x;
		this.y = y;
		this.z = z;
		this.face = face;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.status = in.readByte();
		this.x = in.readInt();
		this.y = in.readUnsignedByte();
		this.z = in.readInt();
		this.face = in.readByte();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.status);
		out.writeInt(this.x);
		out.writeByte(this.y);
		out.writeInt(this.z);
		out.writeByte(this.face);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 14;
	}

}
