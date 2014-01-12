package ch.spacebase.mc.protocol.packet.ingame.server;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerDifficultyPacket implements Packet {
	
	private Difficulty difficulty;
	
	@SuppressWarnings("unused")
	private ServerDifficultyPacket() {
	}
	
	public ServerDifficultyPacket(Difficulty difficulty) {
		this.difficulty = difficulty;
	}
	
	public Difficulty getDifficulty() {
		return this.difficulty;
	}

	@Override
	public void read(NetInput in) throws IOException {
        this.difficulty = Difficulty.values()[in.readUnsignedByte()];
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.difficulty.ordinal());
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

	public static enum Difficulty {
		PEACEFUL,
		EASY,
		NORMAL,
		HARD;
	}

}
