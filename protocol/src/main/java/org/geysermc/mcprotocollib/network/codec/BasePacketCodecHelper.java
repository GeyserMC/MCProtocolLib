package org.geysermc.mcprotocollib.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.nio.charset.StandardCharsets;

public class BasePacketCodecHelper implements PacketCodecHelper {

    @Override
    public void writeVarInt(ByteBuf buf, int value) {
        while ((value & ~0x7F) != 0) {
            buf.writeByte(value & 0x7F | 0x80);
            value >>>= 7;
        }

        buf.writeByte(value);
    }

    @Override
    public int readVarInt(ByteBuf buf) {
        int value = 0;
        int size = 0;

        byte b;
        do {
            if (size >= 35) {
                throw new RuntimeException("VarInt wider than 5 bytes");
            }
            b = buf.readByte();
            value |= (b & 0x7F) << size;
            size += 7;
        } while ((b & 0x80) == 0x80);

        return value;
    }

    @Override
    public void writeVarLong(ByteBuf buf, long value) {
        while ((value & ~0x7FL) != 0) {
            buf.writeByte((int) (value & 0x7F | 0x80));
            value >>>= 7;
        }

        buf.writeByte((int) value);
    }

    @Override
    public long readVarLong(ByteBuf buf) {
        long value = 0;
        int size = 0;

        byte b;
        do {
            if (size >= 70) {
                throw new RuntimeException("VarLong wider than 10 bytes");
            }
            b = buf.readByte();
            value |= (b & 0x7FL) << size;
            size += 7;
        } while ((b & 0x80) == 0x80);

        return value;
    }

    @Override
    public String readString(ByteBuf buf) {
        return this.readString(buf, Short.MAX_VALUE);
    }

    @Override
    public String readString(ByteBuf buf, int maxLength) {
        int length = this.readVarInt(buf);
        if (length > maxLength * 3) {
            throw new IllegalArgumentException("String buffer is longer than maximum allowed length");
        }
        String string = (String) buf.readCharSequence(length, StandardCharsets.UTF_8);
        if (string.length() > maxLength) {
            throw new IllegalArgumentException("String is longer than maximum allowed length");
        }

        return string;
    }

    @Override
    public void writeString(ByteBuf buf, String value) {
        this.writeVarInt(buf, ByteBufUtil.utf8Bytes(value));
        buf.writeCharSequence(value, StandardCharsets.UTF_8);
    }
}
