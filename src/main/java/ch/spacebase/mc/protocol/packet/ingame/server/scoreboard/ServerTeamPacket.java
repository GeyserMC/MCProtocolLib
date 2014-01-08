package ch.spacebase.mc.protocol.packet.ingame.server.scoreboard;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerTeamPacket implements Packet {
	
	private String name;
	private Action action;
	private String displayName;
	private String prefix;
	private String suffix;
	private FriendlyFireMode friendlyFire;
	private String players[];
	
	@SuppressWarnings("unused")
	private ServerTeamPacket() {
	}
	
	public ServerTeamPacket(String name) {
		this.name = name;
		this.action = Action.REMOVE;
	}
	
	public ServerTeamPacket(String name, Action action, String players[]) {
		if(action != Action.ADD_PLAYER && action != Action.REMOVE_PLAYER) {
			throw new IllegalArgumentException("(name, action, players) constructor only valid for adding and removing players.");
		}
		
		this.name = name;
		this.action = action;
		this.players = players;
	}
	
	public ServerTeamPacket(String name, String displayName, String prefix, String suffix, FriendlyFireMode friendlyFire) {
		this.name = name;
		this.displayName = displayName;
		this.prefix = prefix;
		this.suffix = suffix;
		this.friendlyFire = friendlyFire;
		this.action = Action.UPDATE;
	}
	
	public ServerTeamPacket(String name, String displayName, String prefix, String suffix, FriendlyFireMode friendlyFire, String players[]) {
		this.name = name;
		this.displayName = displayName;
		this.prefix = prefix;
		this.suffix = suffix;
		this.friendlyFire = friendlyFire;
		this.players = players;
		this.action = Action.CREATE;
	}
	
	public String getTeamName() {
		return this.name;
	}
	
	public Action getAction() {
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
	
	public FriendlyFireMode getFriendlyFire() {
		return this.friendlyFire;
	}
	
	public String[] getPlayers() {
		return this.players;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.name = in.readString();
		this.action = Action.values()[in.readByte()];
		if(this.action == Action.CREATE || this.action == Action.UPDATE) {
			this.displayName = in.readString();
			this.prefix = in.readString();
			this.suffix = in.readString();
			byte friendlyFire = in.readByte();
			this.friendlyFire = friendlyFire == 3 ? FriendlyFireMode.FRIENDLY_INVISIBLES_VISIBLE : FriendlyFireMode.values()[friendlyFire];
		}
		
		if(this.action == Action.CREATE || this.action == Action.ADD_PLAYER || this.action == Action.REMOVE_PLAYER) {
			this.players = new String[in.readShort()];
			for(int index = 0; index < this.players.length; index++) {
				this.players[index] = in.readString();
			}
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.name);
		out.writeByte(this.action.ordinal());
		if(this.action == Action.CREATE || this.action == Action.UPDATE) {
			out.writeString(this.displayName);
			out.writeString(this.prefix);
			out.writeString(this.suffix);
			out.writeByte(this.friendlyFire == FriendlyFireMode.FRIENDLY_INVISIBLES_VISIBLE ? 3 : this.friendlyFire.ordinal());
		}
		
		if(this.action == Action.CREATE || this.action == Action.ADD_PLAYER || this.action == Action.REMOVE_PLAYER) {
			out.writeShort(this.players.length);
			for(String player : this.players) {
				out.writeString(player);
			}
		}
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static enum Action {
		CREATE,
		REMOVE,
		UPDATE,
		ADD_PLAYER,
		REMOVE_PLAYER;
	}
	
	public static enum FriendlyFireMode {
		OFF,
		ON,
		FRIENDLY_INVISIBLES_VISIBLE;
	}

}
