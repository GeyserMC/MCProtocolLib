package org.geysermc.mcprotocollib.protocol.codec;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.network.codec.PacketCodecHelper;
import org.geysermc.mcprotocollib.network.packet.Packet;

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
    T construct(ByteBuf buf, H codecHelper);
}
