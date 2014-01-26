package ch.spacebase.mc.protocol.packet.ingame.server.scoreboard;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.values.FriendlyFire;
import ch.spacebase.mc.protocol.data.game.values.MagicValues;
import ch.spacebase.mc.protocol.data.game.values.TeamAction;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerTeamPacket implements Packet {
	
	private String name;
	private TeamAction action;
	private String displayName;
	private String prefix;
	private String suffix;
	private FriendlyFire friendlyFire;
	private String players[];
	
	@SuppressWarnings("unused")
	private ServerTeamPacket() {
	}
	
	public ServerTeamPacket(String name) {
		this.name = name;
		this.action = TeamAction.REMOVE;
	}
	
	public ServerTeamPacket(String name, TeamAction action, String players[]) {
		if(action != TeamAction.ADD_PLAYER && action != TeamAction.REMOVE_PLAYER) {
			throw new IllegalArgumentException("(name, action, players) constructor only valid for adding and removing players.");
		}
		
		this.name = name;
		this.action = action;
		this.players = players;
	}
	
	public ServerTeamPacket(String name, String displayName, String prefix, String suffix, FriendlyFire friendlyFire) {
		this.name = name;
		this.displayName = displayName;
		this.prefix = prefix;
		this.suffix = suffix;
		this.friendlyFire = friendlyFire;
		this.action = TeamAction.UPDATE;
	}
	
	public ServerTeamPacket(String name, String displayName, String prefix, String suffix, FriendlyFire friendlyFire, String players[]) {
		this.name = name;
		this.displayName = displayName;
		this.prefix = prefix;
		this.suffix = suffix;
		this.friendlyFire = friendlyFire;
		this.players = players;
		this.action = TeamAction.CREATE;
	}
	
	public String getTeamName() {
		return this.name;
	}
	
	public TeamAction getAction() {
		return this.action;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	public String getPrefix() {
		return this.prefix;
	}
	
	public String getSuffix() {
		return this.suffix;
	}
	
	public FriendlyFire getFriendlyFire() {
		return this.friendlyFire;
	}
	
	public String[] getPlayers() {
		return this.players;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.name = in.readString();
		this.action = MagicValues.key(TeamAction.class, in.readByte());
		if(this.action == TeamAction.CREATE || this.action == TeamAction.UPDATE) {
			this.displayName = in.readString();
			this.prefix = in.readString();
			this.suffix = in.readString();
			this.friendlyFire = MagicValues.key(FriendlyFire.class, in.readByte());
		}
		
		if(this.action == TeamAction.CREATE || this.action == TeamAction.ADD_PLAYER || this.action == TeamAction.REMOVE_PLAYER) {
			this.players = new String[in.readVarInt()];
			for(int index = 0; index < this.players.length; index++) {
				this.players[index] = in.readString();
			}
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.name);
		out.writeByte(MagicValues.value(Integer.class, this.action));
		if(this.action == TeamAction.CREATE || this.action == TeamAction.UPDATE) {
			out.writeString(this.displayName);
			out.writeString(this.prefix);
			out.writeString(this.suffix);
			out.writeByte(MagicValues.value(Integer.class, this.friendlyFire));
		}
		
		if(this.action == TeamAction.CREATE || this.action == TeamAction.ADD_PLAYER || this.action == TeamAction.REMOVE_PLAYER) {
			out.writeVarInt(this.players.length);
			for(String player : this.players) {
				out.writeString(player);
			}
		}
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
