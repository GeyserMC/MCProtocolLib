package com.github.steveice10.packetlib.packet;

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
}
