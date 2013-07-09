package ch.spacebase.mcprotocol.net.io;

import java.io.IOException;

public interface NetOutput {

	/**
	 * Writes a boolean.
	 * @param b Boolean to write.
	 * @throws IOException If an I/O error occurs.
	 */
	public void writeBoolean(boolean b) throws IOException;
	
	/**
	 * Writes a byte.
	 * @param b Byte to write.
	 * @throws IOException If an I/O error occurs.
	 */
	public void writeByte(int b) throws IOException;
	
	/**
	 * Writes a short.
	 * @param s Short to write.
	 * @throws IOException If an I/O error occurs.
	 */
	public void writeShort(int s) throws IOException;
	
	/**
	 * Writes a char.
	 * @param c Char to write.
	 * @throws IOException If an I/O error occurs.
	 */
	public void writeChar(int c) throws IOException;
	
	/**
	 * Writes a integer.
	 * @param i Integer to write.
	 * @throws IOException If an I/O error occurs.
	 */
	public void writeInt(int i) throws IOException;
	
	/**
	 * Writes a long.
	 * @param l Long to write.
	 * @throws IOException If an I/O error occurs.
	 */
	public void writeLong(long l) throws IOException;
	
	/**
	 * Writes a float.
	 * @param f Float to write.
	 * @throws IOException If an I/O error occurs.
	 */
	public void writeFloat(float f) throws IOException;
	
	/**
	 * Writes a double.
	 * @param d Double to write.
	 * @throws IOException If an I/O error occurs.
	 */
	public void writeDouble(double d) throws IOException;
	
	/**
	 * Writes a byte array.
	 * @param b Byte array to write.
	 * @throws IOException If an I/O error occurs.
	 */
	public void writeBytes(byte b[]) throws IOException;
	
	/**
	 * Writes a byte array, using the given amount of bytes.
	 * @param b Byte array to write.
	 * @param length Bytes to write.
	 * @throws IOException If an I/O error occurs.
	 */
	public void writeBytes(byte b[], int length) throws IOException;
	
	/**
	 * Writes a string.
	 * @param s String to write.
	 * @throws IOException If an I/O error occurs.
	 */
	public void writeString(String s) throws IOException;
	
	/**
	 * Flushes the output.
	 * @throws IOException If an I/O error occurs.
	 */
	public void flush() throws IOException;
	
}
