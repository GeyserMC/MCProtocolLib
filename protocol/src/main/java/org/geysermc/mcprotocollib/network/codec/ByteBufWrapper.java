package org.geysermc.mcprotocollib.network.codec;

import io.netty.buffer.ByteBuf;

@FunctionalInterface
public interface ByteBufWrapper<B extends CodecByteBuf> {
    B wrap(ByteBuf buf);
}
