package ch.spacebase.mcprotocol.standard.util;

import java.io.ByteArrayOutputStream;

import ch.spacebase.mcprotocol.standard.packet.PacketPluginMessage;

/**
 * A utility used to build plugin message packets.
 */
public class PluginMessageBuilder {

	/**
	 * The channel of the plugin message.
	 */
	private String channel;
	
	/**
	 * The internal stream used in building the data.
	 */
	private ByteArrayOutputStream out;

	/**
	 * Creates a new plugin message builder.
	 * @param channel Channel of the plugin message.
	 */
	public PluginMessageBuilder(String channel) {
		this.channel = channel;
		this.out = new ByteArrayOutputStream();
	}

	/**
	 * Writes a byte to the plugin message data.
	 * @param b Byte to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeByte(int b) {
		this.out.write(b);
		return this;
	}

	/**
	 * Writes a boolean to the plugin message data.
	 * @param b Boolean to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeBoolean(boolean b) {
		this.writeByte(b ? 1 : (byte) 0);
		return this;
	}

	/**
	 * Writes a short to the plugin message data.
	 * @param s Short to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeShort(int s) {
		this.writeByte((byte) ((s >>> 8) & 0xFF));
		this.writeByte((byte) ((s >>> 0) & 0xFF));
		return this;
	}

	/**
	 * Writes a char to the plugin message data.
	 * @param c Char to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeChar(int c) {
		this.writeByte((byte) ((c >>> 8) & 0xFF));
		this.writeByte((byte) ((c >>> 0) & 0xFF));
		return this;
	}

	/**
	 * Writes an int to the plugin message data.
	 * @param i int to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeInt(int i) {
		this.writeByte((byte) ((i >>> 24) & 0xFF));
		this.writeByte((byte) ((i >>> 16) & 0xFF));
		this.writeByte((byte) ((i >>> 8) & 0xFF));
		this.writeByte((byte) ((i >>> 0) & 0xFF));
		return this;
	}

	/**
	 * Writes a long to the plugin message data.
	 * @param l Long to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeLong(long l) {
		this.writeByte((byte) (l >>> 56));
		this.writeByte((byte) (l >>> 48));
		this.writeByte((byte) (l >>> 40));
		this.writeByte((byte) (l >>> 32));
		this.writeByte((byte) (l >>> 24));
		this.writeByte((byte) (l >>> 16));
		this.writeByte((byte) (l >>> 8));
		this.writeByte((byte) (l >>> 0));
		return this;
	}

	/**
	 * Writes a float to the plugin message data.
	 * @param f Float to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeFloat(float f) {
		this.writeInt(Float.floatToIntBits(f));
		return this;
	}

	/**
	 * Writes a double to the plugin message data.
	 * @param d Double to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeDouble(double d) {
		this.writeLong(Double.doubleToLongBits(d));
		return this;
	}

	/**
	 * Writes a byte array to the plugin message data.
	 * @param b Bytes to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeBytes(byte b[]) {
		this.writeShort(b.length);
		for(byte by : b) {
			this.writeByte(by);
		}

		return this;
	}

	/**
	 * Writes a string to the plugin message data.
	 * @param s String to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeString(String s) {
		int len = s.length();
		if(len >= 65536) {
			throw new IllegalArgumentException("String too long.");
		}

		this.writeShort(len);
		for(int i = 0; i < len; ++i) {
			this.writeChar(s.charAt(i));
		}

		return this;
	}

	/**
	 * Gets the current length of the written data.
	 * @return The data's current length.
	 */
	public int length() {
		return this.out.size();
	}

	/**
	 * Builds a plugin message from the currently stored data.
	 * @return The built plugin message.
	 */
	public PacketPluginMessage build() {
		return new PacketPluginMessage(this.channel, this.out.toByteArray());
	}

}
