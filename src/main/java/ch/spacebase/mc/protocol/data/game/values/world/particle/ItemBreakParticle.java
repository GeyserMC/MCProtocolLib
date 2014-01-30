package ch.spacebase.mc.protocol.data.game.values.world.particle;

public class ItemBreakParticle implements Particle {

	private int id;
	private int data;
	
	public ItemBreakParticle(int id) {
		this(id, -1);
	}
	
	public ItemBreakParticle(int id, int data) {
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
