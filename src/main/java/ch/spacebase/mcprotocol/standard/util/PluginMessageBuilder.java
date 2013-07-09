package ch.spacebase.mcprotocol.standard.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.standard.io.StandardOutput;
import ch.spacebase.mcprotocol.standard.packet.PacketPluginMessage;
import ch.spacebase.mcprotocol.util.Util;

/**
 * A utility used to build plugin message packets.
 */
public class PluginMessageBuilder {

	/**
	 * The channel of the plugin message.
	 */
	private String channel;
	
	/**
	 * The internal output used in building the data.
	 */
	private StandardOutput out;

	/**
	 * Creates a new plugin message builder.
	 * @param channel Channel of the plugin message.
	 */
	public PluginMessageBuilder(String channel) {
		this.channel = channel;
		this.out = new StandardOutput(new ByteArrayOutputStream());
	}

	/**
	 * Writes a byte to the plugin message data.
	 * @param b Byte to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeByte(int b) {
		try {
			this.out.writeByte(b);
		} catch(IOException e) {
			Util.logger().severe("Failed to write to plugin message");
			e.printStackTrace();
		}
		
		return this;
	}

	/**
	 * Writes a boolean to the plugin message data.
	 * @param b Boolean to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeBoolean(boolean b) {
		try {
			this.out.writeBoolean(b);
		} catch(IOException e) {
			Util.logger().severe("Failed to write to plugin message");
			e.printStackTrace();
		}
		
		return this;
	}

	/**
	 * Writes a short to the plugin message data.
	 * @param s Short to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeShort(int s) {
		try {
			this.out.writeShort(s);
		} catch(IOException e) {
			Util.logger().severe("Failed to write to plugin message");
			e.printStackTrace();
		}
		
		return this;
	}

	/**
	 * Writes a char to the plugin message data.
	 * @param c Char to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeChar(int c) {
		try {
			this.out.writeChar(c);
		} catch(IOException e) {
			Util.logger().severe("Failed to write to plugin message");
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * Writes an int to the plugin message data.
	 * @param i int to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeInt(int i) {
		try {
			this.out.writeInt(i);
		} catch(IOException e) {
			Util.logger().severe("Failed to write to plugin message");
			e.printStackTrace();
		}
		
		return this;
	}

	/**
	 * Writes a long to the plugin message data.
	 * @param l Long to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeLong(long l) {
		try {
			this.out.writeLong(l);
		} catch(IOException e) {
			Util.logger().severe("Failed to write to plugin message");
			e.printStackTrace();
		}
		
		return this;
	}

	/**
	 * Writes a float to the plugin message data.
	 * @param f Float to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeFloat(float f) {
		try {
			this.out.writeFloat(f);
		} catch(IOException e) {
			Util.logger().severe("Failed to write to plugin message");
			e.printStackTrace();
		}
		
		return this;
	}

	/**
	 * Writes a double to the plugin message data.
	 * @param d Double to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeDouble(double d) {
		try {
			this.out.writeDouble(d);
		} catch(IOException e) {
			Util.logger().severe("Failed to write to plugin message");
			e.printStackTrace();
		}
		
		return this;
	}

	/**
	 * Writes a byte array to the plugin message data, prepending the array length.
	 * @param b Bytes to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeBytes(byte b[]) {
		try {
			this.out.writeShort(b.length);
			this.out.writeBytes(b);
		} catch(IOException e) {
			Util.logger().severe("Failed to write to plugin message");
			e.printStackTrace();
		}

		return this;
	}

	/**
	 * Writes a string to the plugin message data.
	 * @param s String to write.
	 * @return This plugin message builder.
	 */
	public PluginMessageBuilder writeString(String s) {
		try {
			this.out.writeString(s);
		} catch(IOException e) {
			Util.logger().severe("Failed to write to plugin message");
			e.printStackTrace();
		}

		return this;
	}

	/**
	 * Gets the current length of the written data.
	 * @return The data's current length.
	 */
	public int length() {
		return ((ByteArrayOutputStream) this.out.getStream()).size();
	}

	/**
	 * Builds a plugin message from the currently stored data.
	 * @return The built plugin message.
	 */
	public PacketPluginMessage build() {
		return new PacketPluginMessage(this.channel, ((ByteArrayOutputStream) this.out.getStream()).toByteArray());
	}

}
