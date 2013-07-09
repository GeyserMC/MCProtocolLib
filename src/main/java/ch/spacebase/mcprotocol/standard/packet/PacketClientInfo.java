package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketClientInfo extends Packet {

	public String locale;
	public byte viewDistance;
	public byte chatFlags;
	public byte difficulty;
	public boolean cape;

	public PacketClientInfo() {
	}

	public PacketClientInfo(String locale, byte viewDistance, byte chatFlags, byte difficulty, boolean cape) {
		this.locale = locale;
		this.viewDistance = viewDistance;
		this.chatFlags = chatFlags;
		this.difficulty = difficulty;
		this.cape = cape;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.locale = in.readString();
		this.viewDistance = in.readByte();
		this.chatFlags = in.readByte();
		this.difficulty = in.readByte();
		this.cape = in.readBoolean();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.locale);
		out.writeByte(this.viewDistance);
		out.writeByte(this.chatFlags);
		out.writeByte(this.difficulty);
		out.writeBoolean(this.cape);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 204;
	}

}
