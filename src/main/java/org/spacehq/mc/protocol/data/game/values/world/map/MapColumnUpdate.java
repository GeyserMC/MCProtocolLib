package org.spacehq.mc.protocol.data.game.values.world.map;

public class MapColumnUpdate implements MapData {

	private int x;
	private int y;
	private int height;
	private byte colors[];

	/**
	 * Creates a new map column update instance.
	 * @param x X of the map column.
	 * @param y Y of the data's update range.
	 * @param height Height of the data's update range.
	 * @param colors The array of map color data, arranged in order of ascending Y value relative to the given Y value.
	 */
	public MapColumnUpdate(int x, int y, int height, byte colors[]) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.colors = colors;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getHeight() {
		return this.height;
	}

	public byte[] getColors() {
		return this.colors;
	}

}
