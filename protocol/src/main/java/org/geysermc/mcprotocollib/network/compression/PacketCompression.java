package org.geysermc.mcprotocollib.network.compression;

import io.netty.buffer.ByteBuf;

public interface PacketCompression {
    void inflate(ByteBuf source, ByteBuf destination, int uncompressedSize) throws Exception;

    void deflate(ByteBuf source, ByteBuf destination);

    void close();
}
