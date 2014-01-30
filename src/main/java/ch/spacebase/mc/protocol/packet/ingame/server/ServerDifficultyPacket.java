package ch.spacebase.mc.protocol.packet.ingame.server;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.values.MagicValues;
import ch.spacebase.mc.protocol.data.game.values.setting.Difficulty;
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
        this.difficulty = MagicValues.key(Difficulty.class, in.readUnsignedByte());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(MagicValues.value(Integer.class, this.difficulty));
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
