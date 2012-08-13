package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.util.IOUtils;

public class PacketDisconnect extends Packet {

	public String reason;
	
	public PacketDisconnect() {
	}
	
	public PacketDisconnect(String reason) {
		this.reason = reason;
	}
	
	public String getReason() {
		return this.reason;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.reason = IOUtils.readString(in);
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		IOUtils.writeString(out, this.reason);
	}

	@Override
	public void handleClient(Client conn) {
		conn.disconnect("", false);
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 255;
	}

}
