package org.geysermc.mcprotocollib.network.packet;

import io.netty.buffer.ByteBuf;

/**
 * A network packet. Any given packet must have a constructor that takes in a {@link ByteBuf}.
 */
public interface Packet {

    /**
     * Gets whether the packet has handling priority.
     * If the result is true, the packet will be handled immediately after being
     * decoded.
     *
     * @return Whether the packet has priority.
     */
    default boolean isPriority() {
        return false;
    }

    /**
     * Whether the packet is terminal. That means this should be the last packet sent inside a protocol state.
     * Subsequently, we should disable auto-read when we receive a terminal packet and let the code re-enable auto-read when it's ready to receive more packets.
     *
     * @return Whether the packet is terminal.
     */
    default boolean isTerminal() {
        return false;
    }
}
