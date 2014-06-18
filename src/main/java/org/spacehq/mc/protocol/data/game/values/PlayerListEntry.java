package org.spacehq.mc.protocol.data.game.values;

import org.spacehq.mc.auth.properties.Property;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;

import java.util.UUID;

public class PlayerListEntry {
	private UUID uuid;

	private String name;
	private Property properties[];
	private GameMode gameMode;
	private int ping;

	public PlayerListEntry(UUID uuid, String name, Property properties[], GameMode gameMode, int ping) {
		this.uuid = uuid;
		this.name = name;
		this.properties = properties;
		this.gameMode = gameMode;
		this.ping = ping;
	}

	public PlayerListEntry(UUID uuid, GameMode gameMode) {
		this.uuid = uuid;
		this.gameMode = gameMode;
	}

	public PlayerListEntry(UUID uuid, int ping) {
		this.uuid = uuid;
		this.ping = ping;
	}

	public PlayerListEntry(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public String getName() {
		return this.name;
	}

	public Property[] getProperties() {
		return this.properties;
	}

	public GameMode getGameMode() {
		return this.gameMode;
	}

	public int getPing() {
		return this.ping;
	}
}
