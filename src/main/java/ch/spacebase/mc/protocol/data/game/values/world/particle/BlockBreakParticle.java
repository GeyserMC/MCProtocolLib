package ch.spacebase.mc.protocol.data.game.values.world.particle;

public class BlockBreakParticle implements Particle {

	private int id;
	private int data;
	
	public BlockBreakParticle(int id) {
		this(id, -1);
	}
	
	public BlockBreakParticle(int id, int data) {
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
