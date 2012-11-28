package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketEntityVelocity extends Packet {

	public int entityId;
	public short velX;
	public short velY;
	public short velZ;

	public PacketEntityVelocity() {
	}

	public PacketEntityVelocity(int entityId, short velX, short velY, short velZ) {
		this.entityId = entityId;
		this.velX = velX;
		this.velY = velY;
		this.velZ = velZ;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.entityId = in.readInt();
		this.velX = in.readShort();
		this.velY = in.readShort();
		this.velZ = in.readShort();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.entityId);
		out.writeShort(this.velX);
		out.writeShort(this.velY);
		out.writeShort(this.velZ);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 28;
	}

}
