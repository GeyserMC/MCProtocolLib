package org.spacehq.mc.protocol.data.game.values.world.map;

public class MapPlayer {

	private int centerX;
	private int centerZ;
	private int iconSize;
	private int iconRotation;

	public MapPlayer(int centerX, int centerZ, int iconSize, int iconRotation) {
		this.centerX = centerX;
		this.centerZ = centerZ;
		this.iconSize = iconSize;
		this.iconRotation = iconRotation;
	}

	public int getCenterX() {
		return this.centerX;
	}

	public int getCenterZ() {
		return this.centerZ;
	}

	public int getIconSize() {
		return this.iconSize;
	}

	public int getIconRotation() {
		return this.iconRotation;
	}

}
