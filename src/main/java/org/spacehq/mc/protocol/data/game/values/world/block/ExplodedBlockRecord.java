package org.spacehq.mc.protocol.data.game.values.world.block;

public class ExplodedBlockRecord {

	private int x;
	private int y;
	private int z;

	public ExplodedBlockRecord(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getZ() {
		return this.z;
	}

}
