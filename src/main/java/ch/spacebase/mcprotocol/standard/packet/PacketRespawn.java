package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.util.IOUtils;

public class PacketRespawn extends Packet {

	public byte dimension;
	public byte difficulty;
	public byte gameMode;
	public byte worldHeight;
	public String levelType;
	
	public PacketRespawn() {
	}
	
	public PacketRespawn(byte dimension, byte difficulty, byte gameMode, byte worldHeight, String levelType) {
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.gameMode = gameMode;
		this.worldHeight = worldHeight;
		this.levelType = levelType;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.dimension = in.readByte();
		this.difficulty = in.readByte();
		this.gameMode = in.readByte();
		this.worldHeight = in.readByte();
		this.levelType = IOUtils.readString(in);
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeByte(this.dimension);
		out.writeByte(this.difficulty);
		out.writeByte(this.gameMode);
		out.writeByte(this.worldHeight);
		IOUtils.writeString(out, this.levelType);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 9;
	}
	
}
