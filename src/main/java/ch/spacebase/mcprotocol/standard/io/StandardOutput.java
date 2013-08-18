package ch.spacebase.mcprotocol.standard.io;

import java.io.IOException;
import java.io.OutputStream;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import ch.spacebase.mcprotocol.standard.data.Coordinates;
import ch.spacebase.mcprotocol.standard.data.StandardItemStack;
import ch.spacebase.mcprotocol.standard.data.WatchableObject;
import ch.spacebase.mcprotocol.util.Constants;

public class StandardOutput implements NetOutput {

	private OutputStream out;
	
	public StandardOutput(OutputStream out) {
		this.out = out;
	}
	
	public OutputStream getStream() {
		return this.out;
	}
	
	@Override
	public void writeBoolean(boolean b) throws IOException {
		this.writeByte(b ? 1 : 0);
	}

	@Override
	public void writeByte(int b) throws IOException {
		this.out.write(b);
	}

	@Override
	public void writeShort(int s) throws IOException {
		this.writeByte((byte) ((s >>> 8) & 0xFF));
		this.writeByte((byte) ((s >>> 0) & 0xFF));
	}

	@Override
	public void writeChar(int c) throws IOException {
		this.writeByte((byte) ((c >>> 8) & 0xFF));
		this.writeByte((byte) ((c >>> 0) & 0xFF));
	}

	@Override
	public void writeInt(int i) throws IOException {
		this.writeByte((byte) ((i >>> 24) & 0xFF));
		this.writeByte((byte) ((i >>> 16) & 0xFF));
		this.writeByte((byte) ((i >>> 8) & 0xFF));
		this.writeByte((byte) ((i >>> 0) & 0xFF));
	}

	@Override
	public void writeLong(long l) throws IOException {
		this.writeByte((byte) (l >>> 56));
		this.writeByte((byte) (l >>> 48));
		this.writeByte((byte) (l >>> 40));
		this.writeByte((byte) (l >>> 32));
		this.writeByte((byte) (l >>> 24));
		this.writeByte((byte) (l >>> 16));
		this.writeByte((byte) (l >>> 8));
		this.writeByte((byte) (l >>> 0));
	}

	@Override
	public void writeFloat(float f) throws IOException {
		this.writeInt(Float.floatToIntBits(f));
	}

	@Override
	public void writeDouble(double d) throws IOException {
		this.writeLong(Double.doubleToLongBits(d));
	}

	@Override
	public void writeBytes(byte b[]) throws IOException {
		this.writeBytes(b, b.length);
	}
	
	@Override
	public void writeBytes(byte b[], int length) throws IOException {
		this.out.write(b, 0, length);
	}

	@Override
	public void writeString(String s) throws IOException {
		if(s == null) {
			throw new IllegalArgumentException("String cannot be null!");
		}
		
		int len = s.length();
		if(len >= 65536) {
			throw new IllegalArgumentException("String too long.");
		}

		this.writeShort(len);
		for(int i = 0; i < len; ++i) {
			this.writeChar(s.charAt(i));
		}
	}
	
	@Override
	public void flush() throws IOException {
		this.out.flush();
	}
	
	/**
	 * Writes an entity's metadata.
	 * @param data Metadata objects to write.
	 * @throws IOException If an I/O error occurs.
	 */
	public void writeMetadata(WatchableObject data[]) throws IOException {
		for(WatchableObject obj : data) {
			int header = (obj.getType() << 5 | obj.getType() & 0x1f) & 0xff;
			this.writeByte(header);
			switch(obj.getType()) {
				case Constants.StandardProtocol.WatchableObjectIds.BYTE:
					this.writeByte((Byte) obj.getValue());
					break;
				case Constants.StandardProtocol.WatchableObjectIds.SHORT:
					this.writeShort((Short) obj.getValue());
					break;
				case Constants.StandardProtocol.WatchableObjectIds.INT:
					this.writeInt((Integer) obj.getValue());
					break;
				case Constants.StandardProtocol.WatchableObjectIds.FLOAT:
					this.writeFloat((Float) obj.getValue());
					break;
				case Constants.StandardProtocol.WatchableObjectIds.STRING:
					this.writeString((String) obj.getValue());
					break;
				case Constants.StandardProtocol.WatchableObjectIds.ITEM_STACK:
					this.writeItem((StandardItemStack) obj.getValue());
					break;
				case Constants.StandardProtocol.WatchableObjectIds.COORDINATES:
					this.writeCoordinates((Coordinates) obj.getValue());
					break;
			}
		}

		this.writeByte(127);
	}
	
	/**
	 * Writes an item stack.
	 * @param item Item stack to write.
	 * @throws IOException If an I/O error occurs.
	 */
	public void writeItem(StandardItemStack item) throws IOException {
		this.writeShort(item.getItem());
		if(item.getItem() != -1) {
			this.writeByte(item.getStackSize());
			this.writeShort(item.getDamage());
			if(item.getNBT() != null) {
				this.writeShort(item.getNBT().length);
				this.writeBytes(item.getNBT());
			} else {
				this.writeShort(-1);
			}
		}
	}
	
	/**
	 * Writes a coordinate group.
	 * @param coords Coordinates to write.
	 * @throws IOException If an I/O error occurs.
	 */
	public void writeCoordinates(Coordinates coords) throws IOException {
		this.writeInt(coords.getX());
		this.writeInt(coords.getY());
		this.writeInt(coords.getZ());
	}
	
}
