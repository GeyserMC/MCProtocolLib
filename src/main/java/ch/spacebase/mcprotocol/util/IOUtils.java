package ch.spacebase.mcprotocol.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.spacebase.mcprotocol.standard.data.Coordinates;
import ch.spacebase.mcprotocol.standard.data.ItemStack;
import ch.spacebase.mcprotocol.standard.data.WatchableObject;

/**
 * Contains several {@link DataInputStream}/{@link DataOutputStream}-related
 * utility methods.
 */
public final class IOUtils {

	/**
	 * Reads a string from the given input stream.
	 * @param in Input stream to read from.
	 * @return The resulting string.
	 * @throws IOException If an I/O error occurs.
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
	 * Writes a string to the given output stream.
	 * @param out Output stream to write to.
	 * @param str String to write.
	 * @throws IOException If an I/O error occurs.
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
	 * Reads an entity's metadata from an input stream.
	 * @param in Input stream to read from.
	 * @return The read metadata objects.
	 * @throws IOException If an I/O error occurs.
	 */
	public static WatchableObject[] readMetadata(DataInputStream in) throws IOException {
		List<WatchableObject> objects = new ArrayList<WatchableObject>();

		byte b;
		while((b = in.readByte()) != 127) {
			int type = (b & 0xe0) >> 5;
			int id = b & 0x1f;
			WatchableObject obj = null;

			switch(type) {
				case Constants.StandardProtocol.WatchableObjectIds.BYTE:
					obj = new WatchableObject(type, id, Byte.valueOf(in.readByte()));
					break;
				case Constants.StandardProtocol.WatchableObjectIds.SHORT:
					obj = new WatchableObject(type, id, Short.valueOf(in.readShort()));
					break;
				case Constants.StandardProtocol.WatchableObjectIds.INT:
					obj = new WatchableObject(type, id, Integer.valueOf(in.readInt()));
					break;
				case Constants.StandardProtocol.WatchableObjectIds.FLOAT:
					obj = new WatchableObject(type, id, Float.valueOf(in.readFloat()));
					break;
				case Constants.StandardProtocol.WatchableObjectIds.STRING:
					obj = new WatchableObject(type, id, readString(in));
					break;
				case Constants.StandardProtocol.WatchableObjectIds.ITEM_STACK:
					ItemStack item = new ItemStack();
					item.read(in);
					obj = new WatchableObject(type, id, item);
					break;
				case Constants.StandardProtocol.WatchableObjectIds.COORDINATES:
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

	/**
	 * Writes an entity's metadata to the given output stream.
	 * @param out Output stream to write to.
	 * @param data Metadata objects to write.
	 * @throws IOException If an I/O error occurs.
	 */
	public static void writeMetadata(DataOutputStream out, WatchableObject data[]) throws IOException {
		for(WatchableObject obj : data) {
			int header = (obj.getType() << 5 | obj.getType() & 0x1f) & 0xff;
			out.writeByte(header);

			switch(obj.getType()) {
				case Constants.StandardProtocol.WatchableObjectIds.BYTE:
					out.writeByte((Byte) obj.getValue());
					break;
				case Constants.StandardProtocol.WatchableObjectIds.SHORT:
					out.writeShort((Short) obj.getValue());
					break;
				case Constants.StandardProtocol.WatchableObjectIds.INT:
					out.writeInt((Integer) obj.getValue());
					break;
				case Constants.StandardProtocol.WatchableObjectIds.FLOAT:
					out.writeFloat((Float) obj.getValue());
					break;
				case Constants.StandardProtocol.WatchableObjectIds.STRING:
					writeString(out, (String) obj.getValue());
					break;
				case Constants.StandardProtocol.WatchableObjectIds.ITEM_STACK:
					ItemStack item = (ItemStack) obj.getValue();
					item.write(out);
					break;
				case Constants.StandardProtocol.WatchableObjectIds.COORDINATES:
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