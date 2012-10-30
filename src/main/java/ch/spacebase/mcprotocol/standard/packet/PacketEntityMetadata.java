package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.data.WatchableObject;
import ch.spacebase.mcprotocol.util.IOUtils;

public class PacketEntityMetadata extends Packet {

	public int entityId;
	public WatchableObject metadata[];
	
	public PacketEntityMetadata() {
	}
	
	public PacketEntityMetadata(int entityId, WatchableObject metadata[]) {
		this.entityId = entityId;
		this.metadata = metadata;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.entityId = in.readInt();
		this.metadata = IOUtils.readMetadata(in);
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.entityId);
		IOUtils.writeMetadata(out, this.metadata);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 40;
	}
	
}
