package org.spacehq.packetlib.tcp.io;

import io.netty.buffer.ByteBuf;
import org.spacehq.packetlib.io.NetInput;

import java.io.IOException;

/**
 * A NetInput implementation using a ByteBuf as a backend.
 */
public class ByteBufNetInput implements NetInput {

	private ByteBuf buf;

	public ByteBufNetInput(ByteBuf buf) {
		this.buf = buf;
	}

	@Override
	public boolean readBoolean() throws IOException {
		return this.buf.readBoolean();
	}

	@Override
	public byte readByte() throws IOException {
		return this.buf.readByte();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return this.buf.readUnsignedByte();
	}

	@Override
	public short readShort() throws IOException {
		return this.buf.readShort();
	}

	@Override
	public int readUnsignedShort() throws IOException {
		return this.buf.readUnsignedShort();
	}

	@Override
	public char readChar() throws IOException {
		return this.buf.readChar();
	}

	@Override
	public int readInt() throws IOException {
		return this.buf.readInt();
	}

	@Override
	public int readVarInt() throws IOException {
		int value = 0;
		int size = 0;
		int b;
		while(((b = this.readByte()) & 0x80) != 0) {
			value |= (b & 0x7F) << size;
			size += 7;
			if(size > 35) {
				throw new IOException("Variable length quantity is too long (must be <= 35)");
			}
		}

		return value | (b << size);
	}

	@Override
	public long readLong() throws IOException {
		return this.buf.readLong();
	}

	@Override
	public float readFloat() throws IOException {
		return this.buf.readFloat();
	}

	@Override
	public double readDouble() throws IOException {
		return this.buf.readDouble();
	}

	@Override
	public byte[] readPrefixedBytes() throws IOException {
		short length = this.buf.readShort();
		return this.readBytes(length);
	}

	@Override
	public byte[] readBytes(int length) throws IOException {
		if(length < 0) {
			throw new IllegalArgumentException("Array cannot have length less than 0.");
		}

		byte b[] = new byte[length];
		this.buf.readBytes(b);
		return b;
	}

	@Override
	public String readString() throws IOException {
		int length = this.readVarInt();
		byte bytes[] = this.readBytes(length);
		return new String(bytes, "UTF-8");
	}

	@Override
	public int available() throws IOException {
		return this.buf.readableBytes();
	}

}
