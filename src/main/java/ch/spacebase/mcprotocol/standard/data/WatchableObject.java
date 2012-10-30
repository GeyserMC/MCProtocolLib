package ch.spacebase.mcprotocol.standard.data;

public class WatchableObject {

	private int type;
	private int id;
	private Object value;
	
	public WatchableObject(int type, int id, Object value) {
		this.type = type;
		this.id = id;
		this.value = value;
	}
	
	public int getType() {
		return this.type;
	}
	
	public int getId() {
		return this.id;
	}
	
	public Object getValue() {
		return this.value;
	}

}
