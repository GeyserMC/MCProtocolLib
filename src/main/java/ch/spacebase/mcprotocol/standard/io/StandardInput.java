package ch.spacebase.mcprotocol.standard.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.standard.data.Coordinates;
import ch.spacebase.mcprotocol.standard.data.StandardItemStack;
import ch.spacebase.mcprotocol.standard.data.WatchableObject;
import ch.spacebase.mcprotocol.util.Constants;

public class StandardInput implements NetInput {

	private InputStream in;
	
	public StandardInput(InputStream in) {
		this.in = in;
	}
	
	public InputStream getStream() {
		return this.in;
	}

	@Override
	public boolean readBoolean() throws IOException {
		return this.readByte() == 1;
	}
	
	@Override
	public byte readByte() throws IOException {
		return (byte) this.readUnsignedByte();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		int b = this.in.read();
		if(b < 0) {
			throw new EOFException();
		}
		
		return b;
	}

	@Override
	public short readShort() throws IOException {
		return (short) this.readUnsignedShort();
	}

	@Override
	public int readUnsignedShort() throws IOException {
		int ch1 = this.readUnsignedByte();
		int ch2 = this.readUnsignedByte();
		return (ch1 << 8) + (ch2 << 0);
	}

	@Override
	public char readChar() throws IOException {
		int ch1 = this.readUnsignedByte();
		int ch2 = this.readUnsignedByte();
		return (char) ((ch1 << 8) + (ch2 << 0));
	}

	@Override
	public int readInt() throws IOException {
		int ch1 = this.readUnsignedByte();
		int ch2 = this.readUnsignedByte();
		int ch3 = this.readUnsignedByte();
		int ch4 = this.readUnsignedByte();
		return (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0);
	}

	@Override
	public long readLong() throws IOException {
		byte read[] = this.readBytes(8);
		return ((long) read[0] << 56) + ((long) (read[1] & 255) << 48) + ((long) (read[2] & 255) << 40) + ((long) (read[3] & 255) << 32) + ((long) (read[4] & 255) << 24) + ((read[5] & 255) << 16) + ((read[6] & 255) << 8) + ((read[7] & 255) << 0);
	}

	@Override
	public float readFloat() throws IOException {
		return Float.intBitsToFloat(this.readInt());
	}

	@Override
	public double readDouble() throws IOException {
		return Double.longBitsToDouble(this.readLong());
	}

	@Override
	public byte[] readBytes(int length) throws IOException {
		byte b[] = new byte[length];
		if(length < 0) {
			throw new IndexOutOfBoundsException();
		}
		
		int n = 0;
		while(n < length) {
			int count = this.in.read(b, n, length - n);
			if(count < 0) {
				throw new EOFException();
			}
			
			n += count;
		}
		
		return b;
	}

	@Override
	public String readString() throws IOException {
		int len = this.readUnsignedShort();
		char[] characters = new char[len];
		for(int i = 0; i < len; i++) {
			characters[i] = this.readChar();
		}

		return new String(characters);
	}
	
	@Override
	public int available() throws IOException {
		return this.in.available();
	}
	
	/**
	 * Reads an entity's metadata.
	 * @return The read metadata objects.
	 * @throws IOException If an I/O error occurs.
	 */
	public WatchableObject[] readMetadata() throws IOException {
		List<WatchableObject> objects = new ArrayList<WatchableObject>();
		byte b;
		while((b = this.readByte()) != 127) {
			int type = (b & 0xe0) >> 5;
			int id = b & 0x1f;
			WatchableObject obj = null;

			switch(type) {
				case Constants.StandardProtocol.WatchableObjectIds.BYTE:
					obj = new WatchableObject(type, id, Byte.valueOf(this.readByte()));
					break;
				case Constants.StandardProtocol.WatchableObjectIds.SHORT:
					obj = new WatchableObject(type, id, Short.valueOf(this.readShort()));
					break;
				case Constants.StandardProtocol.WatchableObjectIds.INT:
					obj = new WatchableObject(type, id, Integer.valueOf(this.readInt()));
					break;
				case Constants.StandardProtocol.WatchableObjectIds.FLOAT:
					obj = new WatchableObject(type, id, Float.valueOf(this.readFloat()));
					break;
				case Constants.StandardProtocol.WatchableObjectIds.STRING:
					obj = new WatchableObject(type, id, this.readString());
					break;
				case Constants.StandardProtocol.WatchableObjectIds.ITEM_STACK:
					obj = new WatchableObject(type, id, this.readItem());
					break;
				case Constants.StandardProtocol.WatchableObjectIds.COORDINATES:
					obj = new WatchableObject(type, id, this.readCoordinates());
					break;
			}

			objects.add(obj);
		}

		return objects.toArray(new WatchableObject[objects.size()]);
	}
	
	/**
	 * Reads an item stack.
	 * @return The read item stack.
	 * @throws IOException If an I/O error occurs.
	 */
	public StandardItemStack readItem() throws IOException {
		short item = this.readShort();
		byte stackSize = 1;
		short damage = 0;
		byte nbt[] = null;
		if(item > -1) {
			stackSize = this.readByte();
			damage = this.readShort();
			short length = this.readShort();
			if(length > -1) {
				nbt = this.readBytes(length);
			}
		}
		
		return new StandardItemStack(item, stackSize, damage, nbt);
	}
	
	/**
	 * Reads a coordinate group.
	 * @return The read coordinates.
	 * @throws IOException If an I/O error occurs.
	 */
	public Coordinates readCoordinates() throws IOException {
		int x = this.readInt();
		int y = this.readInt();
		int z = this.readInt();
		return new Coordinates(x, y, z);
	}
	
}
