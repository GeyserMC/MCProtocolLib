package com.github.steveice10.mc.protocol.codec;

import com.github.steveice10.packetlib.codec.PacketCodecHelper;
import com.github.steveice10.packetlib.packet.Packet;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Factory for constructing {@link Packet}s.
 *
 * @param <T> the packet type
 */
@FunctionalInterface
public interface PacketFactory<T extends Packet, H extends PacketCodecHelper> {

    /**
     * Constructs a new {@link Packet}.
     *
     * @param buf         the input buffer
     * @param codecHelper the codec helper
     * @return a new packet from the input
     */
    T construct(ByteBuf buf, H codecHelper) throws IOException;
}
