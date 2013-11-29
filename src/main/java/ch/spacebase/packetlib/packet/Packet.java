package ch.spacebase.packetlib.packet;

import java.nio.ByteBuffer;

/**
 * A network packet.
 */
public interface Packet {

	/**
	 * Gets the id of the packet.
	 * @return The packet's id.
	 */
	public int getId();
	
	/**
	 * Gets the length of the packet.
	 * @return The packet's length.
	 */
	public int getLength();
	
	/**
	 * Reads the packet from the given input buffer.
	 * @param in The buffer to read from.
	 */
	public void read(ByteBuffer in);

	/**
	 * Writes the packet to the given output buffer.
	 * @param in The buffer to write to.
	 */
	public void write(ByteBuffer out);
	
}
