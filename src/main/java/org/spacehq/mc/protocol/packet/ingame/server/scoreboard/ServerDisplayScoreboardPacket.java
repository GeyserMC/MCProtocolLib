package org.spacehq.mc.protocol.packet.ingame.server.scoreboard;

import java.io.IOException;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

public class ServerDisplayScoreboardPacket implements Packet {
	
	private Position position;
	private String name;
	
	@SuppressWarnings("unused")
	private ServerDisplayScoreboardPacket() {
	}
	
	public ServerDisplayScoreboardPacket(Position position, String name) {
		this.position = position;
		this.name = name;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public String getScoreboardName() {
		return this.name;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.position = Position.values()[in.readByte()];
		this.name = in.readString();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.position.ordinal());
		out.writeString(this.name);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static enum Position {
		PLAYER_LIST,
		SIDEBAR,
		BELOW_NAME;
	}

}
