package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketCloseWindow extends Packet {

	public byte id;

	public PacketCloseWindow() {
	}

	public PacketCloseWindow(byte id) {
		this.id = id;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.id = in.readByte();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.id);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 101;
	}

}
