package ch.spacebase.packetlib.io.stream;

import java.io.IOException;
import java.io.OutputStream;

import ch.spacebase.packetlib.io.NetOutput;

/**
 * A NetOutput implementation using an OutputStream as a backend.
 */
public class StreamNetOutput implements NetOutput {

	private OutputStream out;

	public StreamNetOutput(OutputStream out) {
		this.out = out;
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
	public void writeVarInt(int i) throws IOException {
		while((i & -128) != 0) {
			this.writeByte(i & 127 | 128);
			i >>>= 7;
		}

		this.writeByte(i);
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
	public void writePrefixedBytes(byte b[]) throws IOException {
		this.writeShort(b.length);
		this.writeBytes(b);
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

		byte[] bytes = s.getBytes("UTF-8");
		if(bytes.length > 32767) {
			throw new IOException("String too big (was " + s.length() + " bytes encoded, max " + 32767 + ")");
		} else {
			this.writeVarInt(bytes.length);
			this.writeBytes(bytes);
		}
	}

	@Override
	public void flush() throws IOException {
		this.out.flush();
	}

}
