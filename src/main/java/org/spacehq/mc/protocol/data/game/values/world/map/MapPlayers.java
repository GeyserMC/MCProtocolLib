package org.spacehq.mc.protocol.data.game.values.world.map;

import java.util.ArrayList;
import java.util.List;

public class MapPlayers implements MapData {

	private List<MapPlayer> players = new ArrayList<MapPlayer>();

	public MapPlayers(List<MapPlayer> players) {
		this.players = players;
	}

	public List<MapPlayer> getPlayers() {
		return new ArrayList<MapPlayer>(this.players);
	}

}
