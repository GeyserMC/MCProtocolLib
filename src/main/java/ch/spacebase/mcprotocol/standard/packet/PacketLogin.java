package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.util.IOUtils;

public class PacketLogin extends Packet {

	public int entityId;
	public String levelType;
	public byte gameMode;
	public byte dimension;
	public byte difficulty;
	public byte unused;
	public byte maxPlayers;
	
	public PacketLogin() {
	}
	
	public PacketLogin(int entityId, String levelType, byte gameMode, byte dimension, byte difficulty, byte unused, byte maxPlayers) {
		this.entityId = entityId;
		this.levelType = levelType;
		this.gameMode = gameMode;
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.unused = unused;
		this.maxPlayers = maxPlayers;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.entityId = in.readInt();
		this.levelType = IOUtils.readString(in);
		this.gameMode = in.readByte();
		this.dimension = in.readByte();
		this.difficulty = in.readByte();
		this.unused = in.readByte();
		this.maxPlayers = in.readByte();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.entityId);
		IOUtils.writeString(out, this.levelType);
		out.writeByte(this.gameMode);
		out.writeByte(this.dimension);
		out.writeByte(this.difficulty);
		out.writeByte(this.unused);
		out.writeByte(this.maxPlayers);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 1;
	}
	
}
