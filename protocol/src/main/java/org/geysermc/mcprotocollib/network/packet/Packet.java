package org.geysermc.mcprotocollib.network.packet;

import io.netty.buffer.ByteBuf;

/**
 * A network packet. Any given packet must have a constructor that takes in a {@link ByteBuf}.
 */
public interface Packet {

    /**
     * Gets whether the packet terminates the current protocol state.
     * If yes, we disable auto-read if inbound and wait for the code to switch the state.
     *
     * @return Whether the packet terminates the current protocol state
     */
    default boolean isTerminal() {
        return false;
    }
}
