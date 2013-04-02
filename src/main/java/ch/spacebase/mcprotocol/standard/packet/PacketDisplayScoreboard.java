package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.util.IOUtils;

public class PacketDisplayScoreboard extends Packet {

	public byte position;
	public String scoreboard;

	public PacketDisplayScoreboard() {
	}

	public PacketDisplayScoreboard(byte position, String scoreboard) {
		this.position = position;
		this.scoreboard = scoreboard;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.position = in.readByte();
		this.scoreboard = IOUtils.readString(in);
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeByte(this.position);
		IOUtils.writeString(out, this.scoreboard);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 208;
	}

}
