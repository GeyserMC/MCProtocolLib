package ch.spacebase.mc.protocol.data.game;

public class Coordinates {

	private int x;
	private int y;
	private int z;
	
	public Coordinates(int x, int y, int z) {
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
