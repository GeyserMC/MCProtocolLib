package org.geysermc.mcprotocollib.network.packet;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.network.Session;

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
     * Returns whether the packet is terminal. If true, this should be the last packet sent inside a protocol state.
     * Subsequently, {@link Session#setAutoRead(boolean)} should be disabled when a terminal packet is received, until the session has switched into a new state and is ready to receive more packets.
     *
     * @return Whether the packet is terminal.
     */
    default boolean isTerminal() {
        return false;
    }
}
