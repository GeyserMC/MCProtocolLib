package ch.spacebase.mc.protocol.packet.ingame.server.scoreboard;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerUpdateScorePacket implements Packet {
	
	private String name;
	private Action action;
	private String scoreName;
	private int scoreValue;
	
	public ServerUpdateScorePacket() {
	}
	
	public ServerUpdateScorePacket(String name) {
		this.name = name;
		this.action = Action.REMOVE;
	}
	
	public ServerUpdateScorePacket(String name, String scoreName, int scoreValue) {
		this.name = name;
		this.scoreName = scoreName;
		this.scoreValue = scoreValue;
		this.action = Action.ADD_OR_UPDATE;
	}
	
	public String getScoreboardName() {
		return this.name;
	}
	
	public Action getAction() {
		return this.action;
	}
	
	public String getScoreName() {
		return this.scoreName;
	}
	
	public int getScoreValue() {
		return this.scoreValue;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.name = in.readString();
		this.action = Action.values()[in.readByte()];
		if(this.action == Action.ADD_OR_UPDATE) {
			this.scoreName = in.readString();
			this.scoreValue = in.readInt();
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.name);
		out.writeByte(this.action.ordinal());
		if(this.action == Action.ADD_OR_UPDATE) {
			out.writeString(this.scoreName);
			out.writeInt(this.scoreValue);
		}
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static enum Action {
		ADD_OR_UPDATE,
		REMOVE;
	}

}
