package org.spacehq.mc.protocol.data.game.values.world.map;

public class MapPlayer {

	private byte iconSize;
	private byte iconRotation;
	private byte centerX;
	private byte centerZ;

	public MapPlayer(int iconSize, int iconRotation, int centerX, int centerZ) {
		this.iconSize = (byte) iconSize;
		this.iconRotation = (byte) iconRotation;
		this.centerX = (byte) centerX;
		this.centerZ = (byte) centerZ;
	}

	public byte getIconSize() {
		return this.iconSize;
	}

	public byte getIconRotation() {
		return this.iconRotation;
	}

	public byte getCenterX() {
		return this.centerX;
	}

	public byte getCenterZ() {
		return this.centerZ;
	}

}
