package org.spacehq.packetlib.tcp.io;

import io.netty.buffer.ByteBuf;
import org.spacehq.packetlib.io.NetOutput;

import java.io.IOException;
import java.util.UUID;

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
        while((i & ~0x7F) != 0) {
            this.writeByte((i & 0x7F) | 0x80);
            i >>>= 7;
        }

        this.writeByte(i);
    }

    @Override
    public void writeLong(long l) throws IOException {
        this.buf.writeLong(l);
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
        this.buf.writeFloat(f);
    }

    @Override
    public void writeDouble(double d) throws IOException {
        this.buf.writeDouble(d);
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
