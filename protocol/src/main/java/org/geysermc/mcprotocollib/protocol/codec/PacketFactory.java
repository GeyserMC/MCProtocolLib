package org.geysermc.mcprotocollib.protocol.codec;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.network.packet.Packet;

/**
 * Factory for constructing {@link Packet}s.
 *
 * @param <T> the packet type
 */
@FunctionalInterface
public interface PacketFactory<T extends Packet, B extends MinecraftByteBuf> {

    /**
     * Constructs a new {@link Packet}.
     *
     * @param buf the input buffer
     * @return a new packet from the input
     */
    T construct(B buf);
}
