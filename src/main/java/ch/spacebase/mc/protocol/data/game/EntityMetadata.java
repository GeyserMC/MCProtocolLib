package ch.spacebase.mc.protocol.data.game;

import ch.spacebase.mc.protocol.data.game.values.MetadataType;

public class EntityMetadata {

	private int id;
	private MetadataType type;
	private Object value;
	
	public EntityMetadata(int id, MetadataType type, Object value) {
		this.id = id;
		this.type = type;
		this.value = value;
	}
	
	public int getId() {
		return this.id;
	}
	
	public MetadataType getType() {
		return this.type;
	}
	
	public Object getValue() {
		return this.value;
	}
	
}
