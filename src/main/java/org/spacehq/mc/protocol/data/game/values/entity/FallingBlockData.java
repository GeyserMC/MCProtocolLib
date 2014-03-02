package org.spacehq.mc.protocol.data.game.values.entity;

public class FallingBlockData implements ObjectData {

	private int id;
	private int metadata;

	public FallingBlockData(int id, int metadata) {
		this.id = id;
		this.metadata = metadata;
	}

	public int getId() {
		return this.id;
	}

	public int getMetadata() {
		return this.metadata;
	}

}
