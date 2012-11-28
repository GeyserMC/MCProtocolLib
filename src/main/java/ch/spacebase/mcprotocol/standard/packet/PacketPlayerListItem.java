package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.util.IOUtils;

public class PacketPlayerListItem extends Packet {

	public String name;
	public boolean online;
	public short ping;

	public PacketPlayerListItem() {
	}

	public PacketPlayerListItem(String name, boolean online, short ping) {
		this.name = name;
		this.online = online;
		this.ping = ping;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.name = IOUtils.readString(in);
		this.online = in.readBoolean();
		this.ping = in.readShort();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		IOUtils.writeString(out, this.name);
		out.writeBoolean(this.online);
		out.writeShort(this.ping);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 201;
	}

}
