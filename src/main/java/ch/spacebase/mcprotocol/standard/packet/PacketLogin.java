package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketLogin extends Packet {

	public int entityId;
	public String levelType;
	public byte gameMode;
	public int dimension;
	public byte difficulty;
	public byte unused;
	public byte maxPlayers;

        public boolean isVanillaCompatible = true;
        
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
	public void read(NetInput in) throws IOException {
		this.entityId = in.readInt();
		this.levelType = in.readString();
		this.gameMode = in.readByte();
                if (this.isVanillaCompatible) {
                    this.dimension = in.readByte();
                } else {
                    this.dimension = in.readInt();
                }
		this.difficulty = in.readByte();
		this.unused = in.readByte();
		this.maxPlayers = in.readByte();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.entityId);
		out.writeString(this.levelType);
		out.writeByte(this.gameMode);
                if (this.isVanillaCompatible) {
                    out.writeByte(this.dimension);
                } else {
                    out.writeInt(this.dimension);
                }
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
        
        @Override
        public String toString() {
            return "1 [entityID: " + this.entityId + ", levelType: " + this.levelType + ", gameMode: " + this.gameMode + ", dim: " + this.dimension + ", dif: " + this.difficulty + ", usd: " + this.unused + ", maxPlayers: " + this.maxPlayers + "]"; 
        }

}
