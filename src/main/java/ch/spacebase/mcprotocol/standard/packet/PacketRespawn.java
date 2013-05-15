package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.util.IOUtils;

public class PacketRespawn extends Packet {

	public int dimension;
	public byte difficulty;
	public byte gameMode;
	public short worldHeight;
	public String levelType;

	public PacketRespawn() {
	}

	public PacketRespawn(int dimension, byte difficulty, byte gameMode, short worldHeight, String levelType) {
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.gameMode = gameMode;
		this.worldHeight = worldHeight;
		this.levelType = levelType;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.dimension = in.readInt();
		this.difficulty = in.readByte();
		this.gameMode = in.readByte();
		this.worldHeight = in.readShort();
		this.levelType = IOUtils.readString(in);
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.dimension);
		out.writeByte(this.difficulty);
		out.writeByte(this.gameMode);
		out.writeShort(this.worldHeight);
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
