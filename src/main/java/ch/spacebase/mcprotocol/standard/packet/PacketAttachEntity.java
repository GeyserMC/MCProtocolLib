package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketAttachEntity extends Packet {

	public int entityId;
	public int vehicleId;
	
	public PacketAttachEntity() {
	}
	
	public PacketAttachEntity(int entityId, int vehicleId) {
		this.entityId = entityId;
		this.vehicleId = vehicleId;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.entityId = in.readInt();
		this.vehicleId = in.readInt();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.entityId);
		out.writeInt(this.vehicleId);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 39;
	}
	
}
