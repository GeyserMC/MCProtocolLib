package org.geysermc.mcprotocollib.network.packet;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.network.Session;

/**
 * A network packet. Any given packet must have a constructor that takes in a {@link ByteBuf}.
 */
public interface Packet {

    /**
     * Gets whether the packet should run on an async game thread rather than blocking the network (Netty) thread.
     * Packets that qualify for this are usually packets with an ensureRunningOnSameThread call at the top of their packet listener method in the Minecraft code.
     * Packets which need extra attention because they aren't "fully" handled async are marked using
     * // GAME THREAD DETAIL comments in the MCProtocolLib code.
     *
     * @return Whether the packet be handled async from the Netty thread.
     */
    default boolean shouldRunOnGameThread() {
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
