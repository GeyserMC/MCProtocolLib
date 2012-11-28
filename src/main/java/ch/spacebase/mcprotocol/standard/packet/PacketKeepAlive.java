package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketKeepAlive extends Packet {

	public int id;

	public PacketKeepAlive() {
	}

	public PacketKeepAlive(int id) {
		this.id = id;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.id = in.readInt();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.id);
	}

	@Override
	public void handleClient(Client conn) {
		conn.send(new PacketKeepAlive(this.id));
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 0;
	}

}
