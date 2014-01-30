package ch.spacebase.mc.protocol.data.game.values.entity;

public class ProjectileData implements ObjectData{

	private int ownerId;
	
	public ProjectileData(int ownerId) {
		this.ownerId = ownerId;
	}
	
	public int getOwnerId() {
		return this.ownerId;
	}
	
}
