package org.spacehq.packetlib.tcp.io;

import io.netty.buffer.ByteBuf;
import org.spacehq.packetlib.io.NetOutput;

import java.io.IOException;

/**
 * A NetOutput implementation using a ByteBuf as a backend.
 */
public class ByteBufNetOutput implements NetOutput {

	private ByteBuf buf;

	public ByteBufNetOutput(ByteBuf buf) {
		this.buf = buf;
	}

	@Override
	public void writeBoolean(boolean b) throws IOException {
		this.buf.writeBoolean(b);
	}

	@Override
	public void writeByte(int b) throws IOException {
		this.buf.writeByte(b);
	}

	@Override
	public void writeShort(int s) throws IOException {
		this.buf.writeShort(s);
	}

	@Override
	public void writeChar(int c) throws IOException {
		this.buf.writeChar(c);
	}

	@Override
	public void writeInt(int i) throws IOException {
		this.buf.writeInt(i);
	}

	@Override
	public void writeVarInt(int i) throws IOException {
		while((i & 0xFFFFFF80) != 0L) {
			this.writeByte((i & 0x7F) | 0x80);
			i >>>= 7;
		}

		this.writeByte(i & 0x7F);
	}

	@Override
	public void writeLong(long l) throws IOException {
		this.buf.writeLong(l);
	}

	@Override
	public void writeFloat(float f) throws IOException {
		this.buf.writeFloat(f);
	}

	@Override
	public void writeDouble(double d) throws IOException {
		this.buf.writeDouble(d);
	}

	@Override
	public void writePrefixedBytes(byte b[]) throws IOException {
		this.buf.writeShort(b.length);
		this.buf.writeBytes(b);
	}

	@Override
	public void writeBytes(byte b[]) throws IOException {
		this.buf.writeBytes(b);
	}

	@Override
	public void writeBytes(byte b[], int length) throws IOException {
		this.buf.writeBytes(b, 0, length);
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
	}

}
