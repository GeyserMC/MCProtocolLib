package org.spacehq.packetlib.io.stream;

import org.spacehq.packetlib.io.NetInput;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * A NetInput implementation using an InputStream as a backend.
 */
public class StreamNetInput implements NetInput {

	private InputStream in;

	public StreamNetInput(InputStream in) {
		this.in = in;
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
	public int readVarInt() throws IOException {
		int value = 0;
		int size = 0;
		int b;
		while(((b = this.readByte()) & 0x80) == 0x80) {
			value |= (b & 0x7F) << (size++ * 7);
			if(size > 5) {
				throw new IOException("VarInt too long (length must be <= 5)");
			}
		}

		return value | ((b & 0x7F) << (size * 7));
	}

	@Override
	public long readLong() throws IOException {
		byte read[] = this.readBytes(8);
		return ((long) read[0] << 56) + ((long) (read[1] & 255) << 48) + ((long) (read[2] & 255) << 40) + ((long) (read[3] & 255) << 32) + ((long) (read[4] & 255) << 24) + ((read[5] & 255) << 16) + ((read[6] & 255) << 8) + ((read[7] & 255) << 0);
	}

	@Override
	public long readVarLong() throws IOException {
		int value = 0;
		int size = 0;
		int b;
		while(((b = this.readByte()) & 0x80) == 0x80) {
			value |= (b & 0x7F) << (size++ * 7);
			if(size > 10) {
				throw new IOException("VarLong too long (length must be <= 10)");
			}
		}

		return value | ((b & 0x7F) << (size * 7));
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
	public byte[] readPrefixedBytes() throws IOException {
		short length = this.readShort();
		return this.readBytes(length);
	}

	@Override
	public byte[] readBytes(int length) throws IOException {
		if(length < 0) {
			throw new IllegalArgumentException("Array cannot have length less than 0.");
		}

		byte b[] = new byte[length];
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
	public int readBytes(byte[] b) throws IOException {
		return this.in.read(b);
	}

	@Override
	public int readBytes(byte[] b, int offset, int length) throws IOException {
		return this.in.read(b, offset, length);
	}

	@Override
	public String readString() throws IOException {
		int length = this.readVarInt();
		byte bytes[] = this.readBytes(length);
		return new String(bytes, "UTF-8");
	}

	@Override
	public UUID readUUID() throws IOException {
		return new UUID(this.readLong(), this.readLong());
	}

	@Override
	public int available() throws IOException {
		return this.in.available();
	}

}
