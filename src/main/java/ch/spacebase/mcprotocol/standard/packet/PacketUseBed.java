package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketUseBed extends Packet {

	public int entityId;
	public byte unknown;
	public int x;
	public int y;
	public int z;

	public PacketUseBed() {
	}

	public PacketUseBed(int entityId, byte unknown, int x, int y, int z) {
		this.entityId = entityId;
		this.unknown = unknown;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.unknown = in.readByte();
		this.x = in.readInt();
		this.y = in.readUnsignedByte();
		this.z = in.readInt();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte(this.unknown);
		out.writeInt(this.x);
		out.writeByte(this.y);
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
		return 17;
	}

}
