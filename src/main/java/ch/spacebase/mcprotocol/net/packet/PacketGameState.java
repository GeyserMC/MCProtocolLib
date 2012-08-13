package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

public class PacketGameState extends Packet {

	public byte reason;
	public byte gamemode;
	
	public PacketGameState() {
	}
	
	public PacketGameState(byte reason, byte gamemode) {
		this.reason = reason;
		this.gamemode = gamemode;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.reason = in.readByte();
		this.gamemode = in.readByte();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeByte(this.reason);
		out.writeByte(this.gamemode);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 70;
	}
	
}
