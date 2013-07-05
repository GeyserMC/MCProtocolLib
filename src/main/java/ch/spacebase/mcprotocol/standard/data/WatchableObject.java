package ch.spacebase.mcprotocol.standard.data;

/**
 * A watchable object, typically used for entity metadata.
 */
public class WatchableObject {

	/**
	 * The type id of the object.
	 */
	private int type;
	
	/**
	 * The id of the object.
	 */
	private int id;
	
	/**
	 * The value of the object.
	 */
	private Object value;

	/**
	 * Creates a new watchable object.
	 * @param type Type of the object.
	 * @param id Id of the object.
	 * @param value Value of the object.
	 */
	public WatchableObject(int type, int id, Object value) {
		this.type = type;
		this.id = id;
		this.value = value;
	}

	/**
	 * Gets the object's type id.
	 * @return The object's type.
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * Gets the object's id.
	 * @return The object's id.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Gets the object's value.
	 * @return The object's value.
	 */
	public Object getValue() {
		return this.value;
	}

}
