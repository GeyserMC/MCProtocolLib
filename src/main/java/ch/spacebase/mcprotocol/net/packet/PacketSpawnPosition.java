package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

public class PacketSpawnPosition extends Packet {

	public int x;
	public int y;
	public int z;
	
	public PacketSpawnPosition() {
	}
	
	public PacketSpawnPosition(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.x = in.readInt();
		this.y = in.readInt();
		this.z = in.readInt();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
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
		return 6;
	}
	
}
