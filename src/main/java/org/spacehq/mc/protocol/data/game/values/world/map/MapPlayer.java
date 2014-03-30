package org.spacehq.mc.protocol.data.game.values.world.map;

public class MapPlayer {

	private int iconSize;
	private int iconRotation;
	private int centerX;
	private int centerZ;

	public MapPlayer(int iconSize, int iconRotation, int centerX, int centerZ) {
		this.iconSize = iconSize;
		this.iconRotation = iconRotation;
		this.centerX = centerX;
		this.centerZ = centerZ;
	}

	public int getIconSize() {
		return this.iconSize;
	}

	public int getIconRotation() {
		return this.iconRotation;
	}

	public int getCenterX() {
		return this.centerX;
	}

	public int getCenterZ() {
		return this.centerZ;
	}

}
