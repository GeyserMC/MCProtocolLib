package ch.spacebase.mcprotocol.standard.data;

/**
 * Contains a coordinate set.
 */
public class Coordinates {

	/**
	 * The X coordinate.
	 */
	private int x;
	
	/**
	 * The Y coordinate.
	 */
	private int y;
	
	/**
	 * The Z coordinate.
	 */
	private int z;

	/**
	 * Creates a new coordinate set.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 */
	public Coordinates(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Gets the X coordinate.
	 * @return The X coordinate.
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Gets the Y coordinate.
	 * @return The Y coordinate.
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Gets the Z coordinate.
	 * @return The Z coordinate.
	 */
	public int getZ() {
		return this.z;
	}

}
