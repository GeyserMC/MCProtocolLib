package ch.spacebase.mc.protocol.data.game;

public class EntityMetadata {

	private int id;
	private Type type;
	private Object value;
	
	public EntityMetadata(int id, Type type, Object value) {
		this.id = id;
		this.type = type;
		this.value = value;
	}
	
	public int getId() {
		return this.id;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public Object getValue() {
		return this.value;
	}
	
	public static enum Type {
		BYTE,
		SHORT,
		INT,
		FLOAT,
		STRING,
		ITEM,
		COORDINATES;
	}
	
}
