package com.github.steveice10.packetlib.packet;

/**
 * Represents a definition of a packet with various
 * information about it, such as it's id, class and
 * factory for construction.
 *
 * @param <T> the packet type
 */
public class PacketDefinition<T extends Packet> {
    private final int id;
    private final Class<T> packetClass;
    private final PacketFactory<T> factory;

    public PacketDefinition(final int id, final Class<T> packetClass, final PacketFactory<T> factory) {
        this.id = id;
        this.packetClass = packetClass;
        this.factory = factory;
    }

    /**
     * Returns the id of the packet.
     *
     * @return the id of the packet
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the class of the packet.
     *
     * @return the class of the packet
     */
    public Class<T> getPacketClass() {
        return this.packetClass;
    }

    /**
     * Returns the {@link PacketFactory} of the packet.
     *
     * @return the packet factory of the packet
     */
    public PacketFactory<T> getFactory() {
        return this.factory;
    }
}
