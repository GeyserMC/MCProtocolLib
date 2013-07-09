package ch.spacebase.mcprotocol.standard.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.standard.io.StandardInput;
import ch.spacebase.mcprotocol.standard.packet.PacketPluginMessage;

/**
 * A utility for reading plugin message packets.
 */
public class PluginMessageReader {

	/**
	 * The internal stream used to read data.
	 */
	private StandardInput in;
	
	/**
	 * The array being read from.
	 */
	private byte data[];

	/**
	 * Creates a new plugin message reader.
	 * @param packet Packet to read from.
	 */
	public PluginMessageReader(PacketPluginMessage packet) {
		this.in = new StandardInput(new ByteArrayInputStream(packet.data));
		this.data = packet.data;
	}

	/**
	 * Reads the next byte from the message data.
	 * @return The next byte in the array.
	 * @throws IOException If an I/O error occurs.
	 */
	public byte readByte() throws IOException {
		return this.in.readByte();
	}

	/**
	 * Reads the next unsigned byte from the message data.
	 * @return The next unsigned byte in the array.
	 * @throws IOException If an I/O error occurs.
	 */
	public int readUnsignedByte() throws IOException {
		return this.in.readUnsignedByte();
	}

	/**
	 * Reads the next short from the message data.
	 * @return The next short in the array.
	 * @throws IOException If an I/O error occurs.
	 */
	public short readShort() throws IOException {
		return this.in.readShort();
	}

	/**
	 * Reads the unsigned short from the message data.
	 * @return The next unsigned short in the array.
	 * @throws IOException If an I/O error occurs.
	 */
	public int readUnsignedShort() throws IOException {
		return this.in.readUnsignedShort();
	}

	/**
	 * Reads the next char from the message data.
	 * @return The next char in the array.
	 * @throws IOException If an I/O error occurs.
	 */
	public char readChar() throws IOException {
		return this.in.readChar();
	}

	/**
	 * Reads the next int from the message data.
	 * @return The next int in the array.
	 * @throws IOException If an I/O error occurs.
	 */
	public int readInt() throws IOException {
		return this.in.readInt();
	}

	/**
	 * Reads the next long from the message data.
	 * @return The next long in the array.
	 * @throws IOException If an I/O error occurs.
	 */
	public long readLong() throws IOException {
		return this.in.readLong();
	}

	/**
	 * Reads the next float from the message data.
	 * @return The next float in the array.
	 * @throws IOException If an I/O error occurs.
	 */
	public float readFloat() throws IOException {
		return this.in.readFloat();
	}

	/**
	 * Reads the next double from the message data.
	 * @return The next double in the array.
	 * @throws IOException If an I/O error occurs.
	 */
	public double readDouble() throws IOException {
		return this.in.readDouble();
	}

	/**
	 * Reads the given amount of bytes from the message data.
	 * @return An array containing the read bytes.
	 * @throws IOException If an I/O error occurs.
	 */
	public byte[] readBytes(int length) throws IOException {
		return this.in.readBytes(length);
	}

	/**
	 * Reads the next string from the message data.
	 * @return The next string in the array.
	 * @throws IOException If an I/O error occurs.
	 */
	public String readString() throws IOException {
		return this.in.readString();
	}

	/**
	 * Gets the length of the data.
	 * @return The data's length.
	 */
	public int length() {
		return this.data.length;
	}

}
