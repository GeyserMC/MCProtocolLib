package org.spacehq.mc.protocol.data.game.values.world.map;

public class MapData {
	private int columns;
	private int rows;
	private int x;
	private int y;
	private byte data[];

	public MapData(int columns, int rows, int x, int y, byte data[]) {
		this.columns = columns;
		this.rows = rows;
		this.x = x;
		this.y = y;
		this.data = data;
	}

	public int getColumns() {
		return this.columns;
	}

	public int getRows() {
		return this.rows;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public byte[] getData() {
		return this.data;
	}
}
