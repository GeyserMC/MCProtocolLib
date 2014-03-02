package org.spacehq.packetlib.io;

import java.io.IOException;

/**
 * An interface for reading network data.
 */
public interface NetInput {

	/**
	 * Reads the next boolean.
	 *
	 * @return The next boolean.
	 * @throws java.io.IOException If an I/O error occurs.
	 */
	public boolean readBoolean() throws IOException;

	/**
	 * Reads the next byte.
	 *
	 * @return The next byte.
	 * @throws java.io.IOException If an I/O error occurs.
	 */
	public byte readByte() throws IOException;

	/**
	 * Reads the next unsigned byte.
	 *
	 * @return The next unsigned byte.
	 * @throws java.io.IOException If an I/O error occurs.
	 */
	public int readUnsignedByte() throws IOException;

	/**
	 * Reads the next short.
	 *
	 * @return The next short.
	 * @throws java.io.IOException If an I/O error occurs.
	 */
	public short readShort() throws IOException;

	/**
	 * Reads the next unsigned short.
	 *
	 * @return The next unsigned short.
	 * @throws java.io.IOException If an I/O error occurs.
	 */
	public int readUnsignedShort() throws IOException;

	/**
	 * Reads the next char.
	 *
	 * @return The next char.
	 * @throws java.io.IOException If an I/O error occurs.
	 */
	public char readChar() throws IOException;

	/**
	 * Reads the next integer.
	 *
	 * @return The next integer.
	 * @throws java.io.IOException If an I/O error occurs.
	 */
	public int readInt() throws IOException;

	/**
	 * Reads the next varint. A varint is a form of integer where only necessary bytes are written. This is done to save bandwidth.
	 *
	 * @return The next varint.
	 * @throws java.io.IOException If an I/O error occurs.
	 */
	public int readVarInt() throws IOException;

	/**
	 * Reads the next long.
	 *
	 * @return The next long.
	 * @throws java.io.IOException If an I/O error occurs.
	 */
	public long readLong() throws IOException;

	/**
	 * Reads the next float.
	 *
	 * @return The next float.
	 * @throws java.io.IOException If an I/O error occurs.
	 */
	public float readFloat() throws IOException;

	/**
	 * Reads the next double.
	 *
	 * @return The next double.
	 * @throws java.io.IOException If an I/O error occurs.
	 */
	public double readDouble() throws IOException;

	/**
	 * Reads the next byte array, getting the length from a prefixed length value.
	 *
	 * @return The next byte array.
	 * @throws java.io.IOException If an I/O error occurs.
	 */
	public byte[] readPrefixedBytes() throws IOException;

	/**
	 * Reads the next byte array.
	 *
	 * @param The length of the byte array.
	 * @return The next byte array.
	 * @throws java.io.IOException If an I/O error occurs.
	 */
	public byte[] readBytes(int length) throws IOException;

	/**
	 * Reads the next string.
	 *
	 * @return The next string.
	 * @throws java.io.IOException If an I/O error occurs.
	 */
	public String readString() throws IOException;

	/**
	 * Gets the number of available bytes.
	 *
	 * @return The number of available bytes.
	 */
	public int available() throws IOException;

}
