package com.github.steveice10.packetlib.packet;

import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.crypt.PacketEncryption;
import com.github.steveice10.packetlib.io.NetInput;

import java.io.IOException;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * A protocol for packet sending and receiving.
 * All implementations must have a constructor that takes in a {@link NetInput}.
 */
public abstract class PacketProtocol {
    private final Map<Integer, PacketDefinition<? extends Packet>> serverbound = new HashMap<>();
    private final Map<Integer, PacketDefinition<? extends Packet>> clientbound = new HashMap<>();

    private final Map<Class<? extends Packet>, Integer> clientboundIds = new IdentityHashMap<>();
    private final Map<Class<? extends Packet>, Integer> serverboundIds = new IdentityHashMap<>();

    /**
     * Gets the prefix used when locating SRV records for this protocol.
     *
     * @return The protocol's SRV record prefix.
     */
    public abstract String getSRVRecordPrefix();

    /**
     * Gets the packet header of this protocol.
     *
     * @return The protocol's packet header.
     */
    public abstract PacketHeader getPacketHeader();

    /**
     * Gets this protocol's active packet encryption.
     *
     * @return The protocol's packet encryption, or null if packets should not be encrypted.
     */
    public abstract PacketEncryption getEncryption();

    /**
     * Called when a client session is created with this protocol.
     *
     * @param session The created session.
     */
    public abstract void newClientSession(Session session);

    /**
     * Called when a server session is created with this protocol.
     *
     * @param server  The server that the session belongs to.
     * @param session The created session.
     */
    public abstract void newServerSession(Server server, Session session);

    /**
     * Clears all currently registered packets.
     */
    public final void clearPackets() {
        this.serverbound.clear();
        this.clientbound.clear();
        this.clientboundIds.clear();
        this.serverboundIds.clear();
    }

    /**
     * Registers a packet to this protocol as both serverbound and clientbound.
     *
     * @param id      Id to register the packet to.
     * @param packet  Packet to register.
     * @param factory The packet factory.
     * @throws IllegalArgumentException If the packet fails a test creation when being registered as serverbound.
     */
    public final <T extends Packet> void register(int id, Class<T> packet, PacketFactory<T> factory) {
        this.registerServerbound(id, packet, factory);
        this.registerClientbound(id, packet, factory);
    }

    /**
     * Registers a packet to this protocol as both serverbound and clientbound.
     *
     * @param definition The packet definition.
     * @throws IllegalArgumentException If the packet fails a test creation when being registered as serverbound.
     */
    public final void register(PacketDefinition<? extends Packet> definition) {
        this.registerServerbound(definition);
        this.registerClientbound(definition);
    }

    /**
     * Registers a serverbound packet to this protocol.
     *
     * @param id      Id to register the packet to.
     * @param packet  Packet to register.
     * @param factory The packet factory.
     * @throws IllegalArgumentException If the packet fails a test creation.
     */
    public final <T extends Packet> void registerServerbound(int id, Class<T> packet, PacketFactory<T> factory) {
        this.registerServerbound(new PacketDefinition<>(id, packet, factory));
    }

    /**
     * Registers a serverbound packet to this protocol.
     *
     * @param definition The packet definition.
     */
    public final void registerServerbound(PacketDefinition<? extends Packet> definition) {
        this.serverbound.put(definition.getId(), definition);
        this.serverboundIds.put(definition.getPacketClass(), definition.getId());
    }

    /**
     * Registers a clientbound packet to this protocol.
     *
     * @param id     Id to register the packet to.
     * @param packet  Packet to register.
     * @param factory The packet factory.
     */
    public final <T extends Packet> void registerClientbound(int id, Class<T> packet, PacketFactory<T> factory) {
        this.registerClientbound(new PacketDefinition<>(id, packet, factory));
    }

    /**
     * Registers a clientbound packet to this protocol.
     *
     * @param definition The packet definition.
     */
    public final void registerClientbound(PacketDefinition<? extends Packet> definition) {
        this.clientbound.put(definition.getId(), definition);
        this.clientboundIds.put(definition.getPacketClass(), definition.getId());
    }

    /**
     * Creates a new instance of a clientbound packet with the given id and read the clientbound input.
     *
     * @param id Id of the packet to create.
     * @return The created packet.
     * @throws IOException if there was an IO error whilst reading the packet.
     * @throws IllegalArgumentException If the packet ID is not registered.
     */
    public final Packet createClientboundPacket(int id, NetInput in) throws IOException {
        PacketDefinition<?> definition = this.clientbound.get(id);
        if (definition == null) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }

        return definition.getFactory().construct(in);
    }

    /**
     * Gets the registered id of a clientbound packet class.
     *
     * @param packetClass Class of the packet to get the id for.
     * @return The packet's registered id.
     * @throws IllegalArgumentException If the packet is not registered.
     */
    public final int getClientboundId(Class<? extends Packet> packetClass) {
        Integer packetId = this.clientboundIds.get(packetClass);
        if(packetId == null) {
            throw new IllegalArgumentException("Unregistered clientbound packet class: " + packetClass.getName());
        }

        return packetId;
    }

    /**
     * Gets the registered id of a clientbound {@link Packet} instance.
     *
     * @param packet Instance of {@link Packet} to get the id for.
     * @return The packet's registered id.
     * @throws IllegalArgumentException If the packet is not registered.
     */
    public final int getClientboundId(Packet packet) {
        if (packet instanceof BufferedPacket) {
            return getClientboundId(((BufferedPacket) packet).getPacketClass());
        }

        return getClientboundId(packet.getClass());
    }

    /**
     * Gets the packet class for a packet id.
     * @param id The packet id.
     * @return The registered packet's class
     * @throws IllegalArgumentException If the packet ID is not registered.
     */
    public final Class<? extends Packet> getClientboundClass(int id) {
        PacketDefinition<?> definition = this.clientbound.get(id);
        if (definition == null) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }

        return definition.getPacketClass();
    }

    /**
     * Creates a new instance of a serverbound packet with the given id and read the serverbound input.
     *
     * @param id Id of the packet to create.
     * @return The created packet.
     * @throws IOException if there was an IO error whilst reading the packet.
     * @throws IllegalArgumentException If the packet ID is not registered.
     */
    public final Packet createServerboundPacket(int id, NetInput in) throws IOException {
        PacketDefinition<?> definition = this.serverbound.get(id);
        if (definition == null) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }

        return definition.getFactory().construct(in);
    }

    /**
     * Gets the registered id of a serverbound packet class.
     *
     * @param packetClass Class of the packet to get the id for.
     * @return The packet's registered id.
     * @throws IllegalArgumentException If the packet is not registered.
     */
    public final int getServerboundId(Class<? extends Packet> packetClass) {
        Integer packetId = this.serverboundIds.get(packetClass);
        if(packetId == null) {
            throw new IllegalArgumentException("Unregistered serverbound packet class: " + packetClass.getName());
        }

        return packetId;
    }

    /**
     * Gets the registered id of a serverbound {@link Packet} instance.
     *
     * @param packet Instance of {@link Packet} to get the id for.
     * @return The packet's registered id.
     * @throws IllegalArgumentException If the packet is not registered.
     */
    public final int getServerboundId(Packet packet) {
        if (packet instanceof BufferedPacket) {
            return getServerboundId(((BufferedPacket) packet).getPacketClass());
        }

        return getServerboundId(packet.getClass());
    }

    /**
     * Gets the packet class for a packet id.
     * @param id The packet id.
     * @return The registered packet's class
     * @throws IllegalArgumentException If the packet ID is not registered.
     */
    public final Class<? extends Packet> getServerboundClass(int id) {
        PacketDefinition<?> definition = this.serverbound.get(id);
        if (definition == null) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }

        return definition.getPacketClass();
    }
}
