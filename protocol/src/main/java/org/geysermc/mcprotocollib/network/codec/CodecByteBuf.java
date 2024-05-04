package org.geysermc.mcprotocollib.network.codec;

import io.netty.buffer.ByteBuf;

public interface CodecByteBuf {
    void writeVarInt(int value);

    int readVarInt();

    void writeVarLong(long value);

    long readVarLong();

    String readString();

    String readString(int maxLength);

    void writeString(String value);
}
