package org.geysermc.mcprotocollib.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.nio.charset.StandardCharsets;

public class BaseCodecByteBuf extends DelegatedByteBuf implements CodecByteBuf {
    public BaseCodecByteBuf(ByteBuf buf) {
        super(buf);
    }

    @Override
    public void writeVarInt(int value) {
        this.writeVarLong(value & 0xFFFFFFFFL);
    }

    @Override
    public int readVarInt() {
        int value = 0;
        int size = 0;
        int b;
        while (((b = this.readByte()) & 0x80) == 0x80) {
            value |= (b & 0x7F) << (size++ * 7);
            if (size > 5) {
                throw new IllegalArgumentException("VarInt too long (length must be <= 5)");
            }
        }

        return value | ((b & 0x7F) << (size * 7));
    }

    // Based off of Andrew Steinborn's blog post:
    // https://steinborn.me/posts/performance/how-fast-can-you-write-a-varint/
    @Override
    public void writeVarLong(long value) {
        // Peel the one and two byte count cases explicitly as they are the most common VarInt sizes
        // that the server will write, to improve inlining.
        if ((value & ~0x7FL) == 0) {
            this.writeByte((byte) value);
        } else if ((value & ~0x3FFFL) == 0) {
            int w = (int) ((value & 0x7FL | 0x80L) << 8 |
                    (value >>> 7));
            this.writeShort(w);
        } else {
            writeVarLongFull(value);
        }
    }

    private void writeVarLongFull(long value) {
        if ((value & ~0x7FL) == 0) {
            this.writeByte((byte) value);
        } else if ((value & ~0x3FFFL) == 0) {
            int w = (int) ((value & 0x7FL | 0x80L) << 8 |
                    (value >>> 7));
            this.writeShort(w);
        } else if ((value & ~0x1FFFFFL) == 0) {
            int w = (int) ((value & 0x7FL | 0x80L) << 16 |
                    ((value >>> 7) & 0x7FL | 0x80L) << 8 |
                    (value >>> 14));
            this.writeMedium(w);
        } else if ((value & ~0xFFFFFFFL) == 0) {
            int w = (int) ((value & 0x7F | 0x80) << 24 |
                    (((value >>> 7) & 0x7F | 0x80) << 16) |
                    ((value >>> 14) & 0x7F | 0x80) << 8 |
                    (value >>> 21));
            this.writeInt(w);
        } else if ((value & ~0x7FFFFFFFFL) == 0) {
            int w = (int) ((value & 0x7F | 0x80) << 24 |
                    ((value >>> 7) & 0x7F | 0x80) << 16 |
                    ((value >>> 14) & 0x7F | 0x80) << 8 |
                    ((value >>> 21) & 0x7F | 0x80));
            this.writeInt(w);
            this.writeByte((int) (value >>> 28));
        } else if ((value & ~0x3FFFFFFFFFFL) == 0) {
            int w = (int) ((value & 0x7F | 0x80) << 24 |
                    ((value >>> 7) & 0x7F | 0x80) << 16 |
                    ((value >>> 14) & 0x7F | 0x80) << 8 |
                    ((value >>> 21) & 0x7F | 0x80));
            int w2 = (int) (((value >>> 28) & 0x7FL | 0x80L) << 8 |
                    (value >>> 35));
            this.writeInt(w);
            this.writeShort(w2);
        } else if ((value & ~0x1FFFFFFFFFFFFL) == 0) {
            int w = (int) ((value & 0x7F | 0x80) << 24 |
                    ((value >>> 7) & 0x7F | 0x80) << 16 |
                    ((value >>> 14) & 0x7F | 0x80) << 8 |
                    ((value >>> 21) & 0x7F | 0x80));
            int w2 = (int) ((((value >>> 28) & 0x7FL | 0x80L) << 16 |
                    ((value >>> 35) & 0x7FL | 0x80L) << 8) |
                    (value >>> 42));
            this.writeInt(w);
            this.writeMedium(w2);
        } else if ((value & ~0xFFFFFFFFFFFFFFL) == 0) {
            long w = (value & 0x7F | 0x80) << 56 |
                    ((value >>> 7) & 0x7F | 0x80) << 48 |
                    ((value >>> 14) & 0x7F | 0x80) << 40 |
                    ((value >>> 21) & 0x7F | 0x80) << 32 |
                    ((value >>> 28) & 0x7FL | 0x80L) << 24 |
                    ((value >>> 35) & 0x7FL | 0x80L) << 16 |
                    ((value >>> 42) & 0x7FL | 0x80L) << 8 |
                    (value >>> 49);
            this.writeLong(w);
        } else if ((value & ~0x7FFFFFFFFFFFFFFFL) == 0) {
            long w = (value & 0x7F | 0x80) << 56 |
                    ((value >>> 7) & 0x7F | 0x80) << 48 |
                    ((value >>> 14) & 0x7F | 0x80) << 40 |
                    ((value >>> 21) & 0x7F | 0x80) << 32 |
                    ((value >>> 28) & 0x7FL | 0x80L) << 24 |
                    ((value >>> 35) & 0x7FL | 0x80L) << 16 |
                    ((value >>> 42) & 0x7FL | 0x80L) << 8 |
                    (value >>> 49);
            this.writeLong(w);
            this.writeByte((byte) (value >>> 56));
        } else {
            long w = (value & 0x7F | 0x80) << 56 |
                    ((value >>> 7) & 0x7F | 0x80) << 48 |
                    ((value >>> 14) & 0x7F | 0x80) << 40 |
                    ((value >>> 21) & 0x7F | 0x80) << 32 |
                    ((value >>> 28) & 0x7FL | 0x80L) << 24 |
                    ((value >>> 35) & 0x7FL | 0x80L) << 16 |
                    ((value >>> 42) & 0x7FL | 0x80L) << 8 |
                    (value >>> 49);
            int w2 = (int) (((value >>> 56) & 0x7FL | 0x80L) << 8 |
                    (value >>> 63));
            this.writeLong(w);
            this.writeShort(w2);
        }
    }

    @Override
    public long readVarLong() {
        int value = 0;
        int size = 0;
        int b;
        while (((b = this.readByte()) & 0x80) == 0x80) {
            value |= (b & 0x7F) << (size++ * 7);
            if (size > 10) {
                throw new IllegalArgumentException("VarLong too long (length must be <= 10)");
            }
        }

        return value | ((b & 0x7FL) << (size * 7));
    }

    public String readString() {
        return this.readString(Short.MAX_VALUE);
    }

    @Override
    public String readString(int maxLength) {
        int length = this.readVarInt();
        if (length > maxLength * 3) {
            throw new IllegalArgumentException("String thisfer is longer than maximum allowed length");
        }
        String string = (String) this.readCharSequence(length, StandardCharsets.UTF_8);
        if (string.length() > maxLength) {
            throw new IllegalArgumentException("String is longer than maximum allowed length");
        }

        return string;
    }

    @Override
    public void writeString(String value) {
        this.writeVarInt(ByteBufUtil.utf8Bytes(value));
        this.writeCharSequence(value, StandardCharsets.UTF_8);
    }
}
