package ch.spacebase.mcprotocol.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import ch.spacebase.mcprotocol.standard.data.Coordinates;
import ch.spacebase.mcprotocol.standard.data.ItemStack;
import ch.spacebase.mcprotocol.standard.data.WatchableObject;

/**
 * Contains several {@link DataInputStream}-related utility methods.
 */
public final class IOUtils {

	/**
	 * The UTF-8 character set.
	 */
	private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

	/**
	 * Writes a string to the buffer.
	 * 
	 * @param buf
	 *            The buffer.
	 * @param str
	 *            The string.
	 * @throws IllegalArgumentException
	 *             if the string is too long <em>after</em> it is encoded.
	 */
	public static void writeString(DataOutputStream out, String str) throws IOException {
		int len = str.length();
		if(len >= 65536) {
			throw new IllegalArgumentException("String too long.");
		}

		out.writeShort(len);
		for(int i = 0; i < len; ++i) {
			out.writeChar(str.charAt(i));
		}
	}

	/**
	 * Writes a UTF-8 string to the buffer.
	 * 
	 * @param buf
	 *            The buffer.
	 * @param str
	 *            The string.
	 * @throws IllegalArgumentException
	 *             if the string is too long <em>after</em> it is encoded.
	 */
	public static void writeUtf8String(DataOutputStream out, String str) throws IOException {
		byte[] bytes = str.getBytes(CHARSET_UTF8);
		if(bytes.length >= 65536) {
			throw new IllegalArgumentException("Encoded UTF-8 string too long.");
		}

		out.writeShort(bytes.length);
		out.write(bytes);
	}

	/**
	 * Reads a string from the buffer.
	 * 
	 * @param buf
	 *            The buffer.
	 * @return The string.
	 */
	public static String readString(DataInputStream in) throws IOException {
		int len = in.readUnsignedShort();

		char[] characters = new char[len];
		for(int i = 0; i < len; i++) {
			characters[i] = in.readChar();
		}

		return new String(characters);
	}

	/**
	 * Reads a UTF-8 encoded string from the buffer.
	 * 
	 * @param buf
	 *            The buffer.
	 * @return The string.
	 */
	public static String readUtf8String(DataInputStream in) throws IOException {
		int len = in.readUnsignedShort();

		byte[] bytes = new byte[len];
		in.readFully(bytes);

		return new String(bytes, CHARSET_UTF8);
	}

	public static WatchableObject[] readMetadata(DataInputStream in) throws IOException {
		List<WatchableObject> objects = new ArrayList<WatchableObject>();

		byte b;
		while((b = in.readByte()) != 127) {
			int type = (b & 0xe0) >> 5;
			int id = b & 0x1f;
			WatchableObject obj = null;

			switch(type) {
			case 0:
				obj = new WatchableObject(type, id, Byte.valueOf(in.readByte()));
				break;
			case 1:
				obj = new WatchableObject(type, id, Short.valueOf(in.readShort()));
				break;
			case 2:
				obj = new WatchableObject(type, id, Integer.valueOf(in.readInt()));
				break;
			case 3:
				obj = new WatchableObject(type, id, Float.valueOf(in.readFloat()));
				break;
			case 4:
				obj = new WatchableObject(type, id, readString(in));
				break;
			case 5:
				ItemStack item = new ItemStack();
				item.read(in);
				obj = new WatchableObject(type, id, item);
				break;
			case 6:
				int x = in.readInt();
				int y = in.readInt();
				int z = in.readInt();
				obj = new WatchableObject(type, id, new Coordinates(x, y, z));
				break;
			}

			objects.add(obj);
		}

		return objects.toArray(new WatchableObject[objects.size()]);
	}

	public static void writeMetadata(DataOutputStream out, WatchableObject data[]) throws IOException {
		for(WatchableObject obj : data) {
			int header = (obj.getType() << 5 | obj.getType() & 0x1f) & 0xff;
			out.writeByte(header);

			switch(obj.getType()) {
			case 0:
				out.writeByte((Byte) obj.getValue());
				break;
			case 1:
				out.writeShort((Short) obj.getValue());
				break;
			case 2:
				out.writeInt((Integer) obj.getValue());
				break;
			case 3:
				out.writeFloat((Float) obj.getValue());
				break;
			case 4:
				writeString(out, (String) obj.getValue());
				break;
			case 5:
				ItemStack item = (ItemStack) obj.getValue();
				item.write(out);
				break;
			case 6:
				Coordinates chunkcoordinates = (Coordinates) obj.getValue();
				out.writeInt(chunkcoordinates.getX());
				out.writeInt(chunkcoordinates.getY());
				out.writeInt(chunkcoordinates.getZ());
				break;
			}
		}

		out.writeByte(127);
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private IOUtils() {

	}

}