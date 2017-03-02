package com.github.steveice10.packetlib.io.stream;

import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * A NetOutput implementation using an OutputStream as a backend.
 */
public class StreamNetOutput implements NetOutput {
    private OutputStream out;

    /**
     * Creates a new StreamNetOutput instance.
     *
     * @param out OutputStream to write to.
     */
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
        while((i & ~0x7F) != 0) {
            this.writeByte((i & 0x7F) | 0x80);
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
    public void writeVarLong(long l) throws IOException {
        while((l & ~0x7F) != 0) {
            this.writeByte((int) (l & 0x7F) | 0x80);
            l >>>= 7;
        }

        this.writeByte((int) l);
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
        this.out.flush();
    }
}
