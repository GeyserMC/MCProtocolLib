package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

public class PacketEntityStatus extends Packet {

	public int entityId;
	public byte status;
	
	public PacketEntityStatus() {
	}
	
	public PacketEntityStatus(int entityId, byte status) {
		this.entityId = entityId;
		this.status = status;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.entityId = in.readInt();
		this.status = in.readByte();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte(this.status);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 38;
	}
	
}
