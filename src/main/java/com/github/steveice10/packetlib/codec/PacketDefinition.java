package com.github.steveice10.packetlib.codec;

import com.github.steveice10.packetlib.packet.Packet;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Represents a definition of a packet with various
 * information about it, such as it's id, class and
 * factory for construction.
 *
 * @param <T>         the packet type
 * @param <H>         the codec helper type
 * @param id          the packet id
 * @param packetClass the packet class
 * @param serializer  the packet serializer
 */
public record PacketDefinition<T extends Packet, H extends PacketCodecHelper>(int id, Class<T> packetClass,
                                                                              PacketSerializer<T, H> serializer) {
    public T newInstance(ByteBuf buf, H helper) throws IOException {
        return this.serializer.deserialize(buf, helper, this);
    }
}
