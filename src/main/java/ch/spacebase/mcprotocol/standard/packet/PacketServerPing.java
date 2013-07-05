package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.util.Constants;

public class PacketServerPing extends Packet {

	public boolean newFormat = false;

	public PacketServerPing() {
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		if(in.available() > 0) {
			in.readByte();
			this.newFormat = true;
		} else {
			this.newFormat = false;
		}
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeByte(Constants.StandardProtocol.PING_MAGIC);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 254;
	}

}
