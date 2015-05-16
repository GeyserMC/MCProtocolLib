package org.spacehq.mc.protocol.packet.ingame.server;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.setting.Difficulty;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

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

	public void read(NetInput in) throws IOException {
		this.difficulty = MagicValues.key(Difficulty.class, in.readUnsignedByte());
	}

	public void write(NetOutput out) throws IOException {
		out.writeByte(MagicValues.value(Integer.class, this.difficulty));
	}

	public boolean isPriority() {
		return false;
	}

}
