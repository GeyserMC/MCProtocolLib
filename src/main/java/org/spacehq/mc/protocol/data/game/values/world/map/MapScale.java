package org.spacehq.mc.protocol.data.game.values.world.map;

public class MapScale implements MapData {

	private byte scale;

	public MapScale(int scale) {
		this.scale = (byte) scale;
	}

	public byte getScale() {
		return this.scale;
	}

}
