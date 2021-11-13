package com.github.steveice10.packetlib.packet;

import com.github.steveice10.packetlib.io.NetInput;

import java.io.IOException;

/**
 * Factory for constructing {@link Packet}s.
 *
 * @param <T> the packet type
 */
@FunctionalInterface
public interface PacketFactory<T extends Packet> {

    /**
     * Constructs a new {@link Packet} from the
     * given {@link NetInput}.
     *
     * @param input the input
     * @return a new packet from the input
     */
    T construct(NetInput input) throws IOException;
}
