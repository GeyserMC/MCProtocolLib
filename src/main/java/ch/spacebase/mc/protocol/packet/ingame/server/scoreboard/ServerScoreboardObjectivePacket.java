package ch.spacebase.mc.protocol.packet.ingame.server.scoreboard;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerScoreboardObjectivePacket implements Packet {
	
	private String name;
	private String displayName;
	private Action action;
	
	@SuppressWarnings("unused")
	private ServerScoreboardObjectivePacket() {
	}
	
	public ServerScoreboardObjectivePacket(String name, String displayName, Action action) {
		this.name = name;
		this.displayName = displayName;
		this.action = action;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	public Action getAction() {
		return this.action;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.name = in.readString();
		this.displayName = in.readString();
		this.action = Action.values()[in.readByte()];
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.name);
		out.writeString(this.displayName);
		out.writeByte(this.action.ordinal());
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static enum Action {
		ADD,
		REMOVE,
		UPDATE;
	}

}
