package ch.spacebase.mcprotocol.standard.util;

import java.io.ByteArrayInputStream;
import java.io.EOFException;

import ch.spacebase.mcprotocol.standard.packet.PacketPluginMessage;

/**
 * A utility for reading plugin message packets.
 */
public class PluginMessageReader {

	/**
	 * The internal stream used to read data.
	 */
	private ByteArrayInputStream in;
	
	/**
	 * The array being read from.
	 */
	private byte data[];

	/**
	 * Creates a new plugin message reader.
	 * @param packet Packet to read from.
	 */
	public PluginMessageReader(PacketPluginMessage packet) {
		this.in = new ByteArrayInputStream(packet.data);
		this.data = packet.data;
	}

	/**
	 * Reads the next byte from the message data.
	 * @return The next byte in the array.
	 * @throws EOFException If the end of the array has been reached.
	 */
	public final byte readByte() throws EOFException {
		return (byte) this.readUnsignedByte();
	}

	/**
	 * Reads the next unsigned byte from the message data.
	 * @return The next unsigned byte in the array.
	 * @throws EOFException If the end of the array has been reached.
	 */
	public final int readUnsignedByte() throws EOFException {
		int b = this.in.read();
		if(b < 0) {
			throw new EOFException("Reached end of data.");
		}

		return b;
	}

	/**
	 * Reads the next short from the message data.
	 * @return The next short in the array.
	 * @throws EOFException If the end of the array has been reached.
	 */
	public final short readShort() throws EOFException {
		return (short) this.readUnsignedShort();
	}

	/**
	 * Reads the unsigned short from the message data.
	 * @return The next unsigned short in the array.
	 * @throws EOFException If the end of the array has been reached.
	 */
	public final int readUnsignedShort() throws EOFException {
		int ch1 = this.readUnsignedByte();
		int ch2 = this.readUnsignedByte();
		return (ch1 << 8) + (ch2 << 0);
	}

	/**
	 * Reads the next char from the message data.
	 * @return The next char in the array.
	 * @throws EOFException If the end of the array has been reached.
	 */
	public final char readChar() throws EOFException {
		int ch1 = this.readUnsignedByte();
		int ch2 = this.readUnsignedByte();
		return (char) ((ch1 << 8) + (ch2 << 0));
	}

	/**
	 * Reads the next int from the message data.
	 * @return The next int in the array.
	 * @throws EOFException If the end of the array has been reached.
	 */
	public final int readInt() throws EOFException {
		int ch1 = this.readUnsignedByte();
		int ch2 = this.readUnsignedByte();
		int ch3 = this.readUnsignedByte();
		int ch4 = this.readUnsignedByte();
		return (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0);
	}

	/**
	 * Reads the next long from the message data.
	 * @return The next long in the array.
	 * @throws EOFException If the end of the array has been reached.
	 */
	public final long readLong() throws EOFException {
		byte read[] = this.readBytes(8);
		return ((long) read[0] << 56) + ((long) (read[1] & 255) << 48) + ((long) (read[2] & 255) << 40) + ((long) (read[3] & 255) << 32) + ((long) (read[4] & 255) << 24) + ((read[5] & 255) << 16) + ((read[6] & 255) << 8) + ((read[7] & 255) << 0);
	}

	/**
	 * Reads the next float from the message data.
	 * @return The next float in the array.
	 * @throws EOFException If the end of the array has been reached.
	 */
	public final float readFloat() throws EOFException {
		return Float.intBitsToFloat(this.readInt());
	}

	/**
	 * Reads the next double from the message data.
	 * @return The next double in the array.
	 * @throws EOFException If the end of the array has been reached.
	 */
	public final double readDouble() throws EOFException {
		return Double.longBitsToDouble(this.readLong());
	}
	
	/**
	 * Reads the next byte array from the message data.
	 * @return The next byte array in the array.
	 * @throws EOFException If the end of the array has been reached.
	 */
	public byte[] readBytes() throws EOFException {
		return this.readBytes(this.readUnsignedShort());
	}

	/**
	 * Reads the given amount of bytes from the message data.
	 * @return An array containing the read bytes.
	 * @throws EOFException If the end of the array has been reached.
	 */
	public byte[] readBytes(int length) throws EOFException {
		return this.readBytes(length, 0);
	}

	/**
	 * Reads the given amount of bytes from the message data at the given offset.
	 * @return An array containing the read bytes.
	 * @throws EOFException If the end of the array has been reached.
	 */
	public byte[] readBytes(int length, int offset) throws EOFException {
		byte b[] = new byte[length];
		if(this.in.read(b, offset, length) != length) {
			throw new EOFException("Reached end of data.");
		}

		return b;
	}

	/**
	 * Reads the next string from the message data.
	 * @return The next string in the array.
	 * @throws EOFException If the end of the array has been reached.
	 */
	public String readString() throws EOFException {
		int len = this.readUnsignedShort();
		char[] characters = new char[len];
		for(int i = 0; i < len; i++) {
			characters[i] = this.readChar();
		}

		return new String(characters);
	}

	/**
	 * Gets the length of the data.
	 * @return The data's length.
	 */
	public int length() {
		return this.data.length;
	}

}
