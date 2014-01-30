package ch.spacebase.mc.protocol.packet.ingame.server.scoreboard;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.values.MagicValues;
import ch.spacebase.mc.protocol.data.game.values.scoreboard.ObjectiveAction;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerScoreboardObjectivePacket implements Packet {
	
	private String name;
	private String displayName;
	private ObjectiveAction action;
	
	@SuppressWarnings("unused")
	private ServerScoreboardObjectivePacket() {
	}
	
	public ServerScoreboardObjectivePacket(String name, String displayName, ObjectiveAction action) {
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
	
	public ObjectiveAction getAction() {
		return this.action;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.name = in.readString();
		this.displayName = in.readString();
		this.action = MagicValues.key(ObjectiveAction.class, in.readByte());
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.name);
		out.writeString(this.displayName);
		out.writeByte(MagicValues.value(Integer.class, this.action));
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
