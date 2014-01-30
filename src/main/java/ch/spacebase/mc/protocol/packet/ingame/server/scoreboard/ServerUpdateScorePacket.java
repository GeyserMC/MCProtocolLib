package ch.spacebase.mc.protocol.packet.ingame.server.scoreboard;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.values.MagicValues;
import ch.spacebase.mc.protocol.data.game.values.scoreboard.ScoreboardAction;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerUpdateScorePacket implements Packet {
	
	private String entry;
	private ScoreboardAction action;
	private String objective;
	private int value;
	
	@SuppressWarnings("unused")
	private ServerUpdateScorePacket() {
	}
	
	public ServerUpdateScorePacket(String entry) {
		this.entry = entry;
		this.action = ScoreboardAction.REMOVE;
	}
	
	public ServerUpdateScorePacket(String entry, String objective, int value) {
		this.entry = entry;
		this.objective = objective;
		this.value = value;
		this.action = ScoreboardAction.ADD_OR_UPDATE;
	}
	
	public String getEntry() {
		return this.entry;
	}
	
	public ScoreboardAction getAction() {
		return this.action;
	}
	
	public String getObjective() {
		return this.objective;
	}
	
	public int getValue() {
		return this.value;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.entry = in.readString();
		this.action = MagicValues.key(ScoreboardAction.class, in.readByte());
		if(this.action == ScoreboardAction.ADD_OR_UPDATE) {
			this.objective = in.readString();
			this.value = in.readVarInt();
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.entry);
		out.writeByte(MagicValues.value(Integer.class, this.action));
		if(this.action == ScoreboardAction.ADD_OR_UPDATE) {
			out.writeString(this.objective);
			out.writeVarInt(this.value);
		}
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
