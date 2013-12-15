package ch.spacebase.mc.protocol.data.status;

import ch.spacebase.mc.auth.GameProfile;

public class PlayerInfo {

	private int max;
	private int online;
	private GameProfile players[];
	
	public PlayerInfo(int max, int online, GameProfile players[]) {
		this.max = max;
		this.online = online;
		this.players = players;
	}
	
	public int getMaxPlayers() {
		return this.max;
	}
	
	public int getOnlinePlayers() {
		return this.online;
	}
	
	public GameProfile[] getPlayers() {
		return this.players;
	}
	
}
