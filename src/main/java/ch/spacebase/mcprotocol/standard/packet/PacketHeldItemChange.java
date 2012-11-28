package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketHeldItemChange extends Packet {

	public short slot;

	public PacketHeldItemChange() {
	}

	public PacketHeldItemChange(short slot) {
		this.slot = slot;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.slot = in.readShort();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeShort(this.slot);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 16;
	}

}
