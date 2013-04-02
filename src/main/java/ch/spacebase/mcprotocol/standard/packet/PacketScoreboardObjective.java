package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.util.IOUtils;

public class PacketScoreboardObjective extends Packet {

	public String name;
	public String value;
	public byte action;

	public PacketScoreboardObjective() {
	}

	public PacketScoreboardObjective(String name, String value, byte action) {
		this.name = name;
		this.value = value;
		this.action = action;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.name = IOUtils.readString(in);
		this.value = IOUtils.readString(in);
		this.action = in.readByte();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		IOUtils.writeString(out, this.name);
		IOUtils.writeString(out, this.value);
		out.writeByte(this.action);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 206;
	}

}
