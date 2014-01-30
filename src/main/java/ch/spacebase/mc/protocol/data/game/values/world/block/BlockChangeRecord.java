package ch.spacebase.mc.protocol.data.game.values.world.block;

import ch.spacebase.mc.protocol.data.game.Position;

public class BlockChangeRecord {

	private Position position;
	private int id;
	private int metadata;
	
	public BlockChangeRecord(Position position, int id, int metadata) {
		this.position = position;
		this.metadata = metadata;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getMetadata() {
		return this.metadata;
	}
	
}
