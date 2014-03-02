package org.spacehq.mc.protocol.data.game.values.world.map;

public class MapColumnUpdate implements MapData {

	private byte x;
	private byte y;
	private byte height;
	private byte colors[];

	/**
	 * Creates a new map column update instance.
	 *
	 * @param x             X of the map column.
	 * @param y             Y of the data's update range.
	 * @param height        Height of the data's update range.
	 * @param fullMapColors The full array of map color data, arranged in order of ascending Y value relative to the given Y value.
	 */
	public MapColumnUpdate(int x, int y, int height, byte colors[]) {
		this.x = (byte) x;
		this.y = (byte) y;
		this.height = (byte) height;
		this.colors = colors;
	}

	public byte getX() {
		return this.x;
	}

	public byte getY() {
		return this.y;
	}

	public byte getHeight() {
		return this.height;
	}

	public byte[] getColors() {
		return this.colors;
	}

}
