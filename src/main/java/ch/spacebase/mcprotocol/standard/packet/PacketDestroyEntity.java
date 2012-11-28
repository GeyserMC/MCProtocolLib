package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketDestroyEntity extends Packet {

	public int entityIds[];

	public PacketDestroyEntity() {
	}

	public PacketDestroyEntity(int... entityIds) {
		this.entityIds = entityIds;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.entityIds = new int[in.readByte()];
		for(int count = 0; count < this.entityIds.length; count++) {
			this.entityIds[count] = in.readInt();
		}
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeByte(this.entityIds.length);
		for(int id : this.entityIds) {
			out.writeInt(id);
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
		return 29;
	}

}
