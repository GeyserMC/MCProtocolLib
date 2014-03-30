package org.spacehq.mc.protocol.data.game.values.world.map;

public class MapScale implements MapData {

	private int scale;

	public MapScale(int scale) {
		this.scale = scale;
	}

	public int getScale() {
		return this.scale;
	}

}
