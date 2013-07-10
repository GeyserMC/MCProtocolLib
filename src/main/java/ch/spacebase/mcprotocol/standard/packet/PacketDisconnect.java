package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

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
	public void read(NetInput in) throws IOException {
		this.reason = in.readString();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.reason);
	}

	@Override
	public void handleClient(Client conn) {
		conn.disconnect(this.reason, false);
	}

	@Override
	public void handleServer(ServerConnection conn) {
		conn.disconnect(this.reason, false);
	}

	@Override
	public int getId() {
		return 255;
	}

}
