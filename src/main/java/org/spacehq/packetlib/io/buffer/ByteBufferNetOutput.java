package org.spacehq.packetlib.io.buffer;

import org.spacehq.packetlib.io.NetOutput;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * A NetOutput implementation using a ByteBuf as a backend.
 */
public class ByteBufferNetOutput implements NetOutput {
    private ByteBuffer buffer;

    public ByteBufferNetOutput(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public ByteBuffer getByteBuffer() {
        return this.buffer;
    }

    @Override
    public void writeBoolean(boolean b) throws IOException {
        this.buffer.put(b ? (byte) 1 : 0);
    }

    @Override
    public void writeByte(int b) throws IOException {
        this.buffer.put((byte) b);
    }

    @Override
    public void writeShort(int s) throws IOException {
        this.buffer.putShort((short) s);
    }

    @Override
    public void writeChar(int c) throws IOException {
        this.buffer.putChar((char) c);
    }

    @Override
    public void writeInt(int i) throws IOException {
        this.buffer.putInt(i);
    }

    @Override
    public void writeVarInt(int i) throws IOException {
        while((i & ~0x7F) != 0) {
            this.writeByte((i & 0x7F) | 0x80);
            i >>>= 7;
        }

        this.writeByte(i);
    }

    @Override
    public void writeLong(long l) throws IOException {
        this.buffer.putLong(l);
    }

    @Override
    public void writeVarLong(long l) throws IOException {
        while((l & ~0x7F) != 0) {
            this.writeByte((int) (l & 0x7F) | 0x80);
            l >>>= 7;
        }

        this.writeByte((int) l);
    }

    @Override
    public void writeFloat(float f) throws IOException {
        this.buffer.putFloat(f);
    }

    @Override
    public void writeDouble(double d) throws IOException {
        this.buffer.putDouble(d);
    }

    @Override
    public void writeBytes(byte b[]) throws IOException {
        this.buffer.put(b);
    }

    @Override
    public void writeBytes(byte b[], int length) throws IOException {
        this.buffer.put(b, 0, length);
    }

    @Override
    public void writeShorts(short[] s) throws IOException {
        this.writeShorts(s, s.length);
    }

    @Override
    public void writeShorts(short[] s, int length) throws IOException {
        for(int index = 0; index < length; index++) {
            this.writeShort(s[index]);
        }
    }

    @Override
    public void writeInts(int[] i) throws IOException {
        this.writeInts(i, i.length);
    }

    @Override
    public void writeInts(int[] i, int length) throws IOException {
        for(int index = 0; index < length; index++) {
            this.writeInt(i[index]);
        }
    }

    @Override
    public void writeLongs(long[] l) throws IOException {
        this.writeLongs(l, l.length);
    }

    @Override
    public void writeLongs(long[] l, int length) throws IOException {
        for(int index = 0; index < length; index++) {
            this.writeLong(l[index]);
        }
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
    public void writeUUID(UUID uuid) throws IOException {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
    }

    @Override
    public void flush() throws IOException {
    }
}
