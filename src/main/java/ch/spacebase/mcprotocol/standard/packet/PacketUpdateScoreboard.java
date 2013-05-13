package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.util.IOUtils;

public class PacketUpdateScoreboard extends Packet {

	public String item;
	public byte action;
	public String scoreboard;
	public int value;

	public PacketUpdateScoreboard() {
	}

	public PacketUpdateScoreboard(String item, byte action, String scoreboard, int value) {
		this.item = item;
		this.action = action;
		this.scoreboard = scoreboard;
		this.value = value;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.item = IOUtils.readString(in);
		this.action = in.readByte();
		if(this.action != 1) {
			this.scoreboard = IOUtils.readString(in);
			this.value = in.readInt();
		}
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		IOUtils.writeString(out, this.item);
		out.writeByte(this.action);
		if(this.action != 1) {
			IOUtils.writeString(out, this.scoreboard);
			out.writeInt(this.value);
		}
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 207;
	}

}
