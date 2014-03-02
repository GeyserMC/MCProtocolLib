package org.spacehq.mc.protocol.data.game.values.world.particle;

public class BlockImpactParticle implements Particle {

	private int id;
	private int data;

	public BlockImpactParticle(int id) {
		this(id, -1);
	}

	public BlockImpactParticle(int id, int data) {
		this.id = id;
		this.data = data;
	}

	public int getId() {
		return this.id;
	}

	public int getData() {
		return this.data;
	}

}
