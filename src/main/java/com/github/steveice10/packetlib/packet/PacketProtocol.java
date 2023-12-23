package com.github.steveice10.packetlib.packet;

import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.codec.PacketCodecHelper;
import com.github.steveice10.packetlib.codec.PacketDefinition;
import com.github.steveice10.packetlib.codec.PacketSerializer;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * A protocol for packet sending and receiving.
 * All implementations must have a constructor that takes in a {@link ByteBuf}.
 */
public abstract class PacketProtocol {
    private final Int2ObjectMap<PacketDefinition<? extends Packet, ?>> serverbound = new Int2ObjectOpenHashMap<>();
    private final Int2ObjectMap<PacketDefinition<? extends Packet, ?>> clientbound = new Int2ObjectOpenHashMap<>();

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
     * Creates a new {@link PacketCodecHelper} that can be used
     * for each session.
     *
     * @return A new {@link PacketCodecHelper}.
     */
    public abstract PacketCodecHelper createHelper();

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
     * @param id         Id to register the packet to.
     * @param packet     Packet to register.
     * @param serializer The packet serializer.
     * @throws IllegalArgumentException If the packet fails a test creation when being registered as serverbound.
     */
    public final <T extends Packet, H extends PacketCodecHelper> void register(int id, Class<T> packet, PacketSerializer<T, H> serializer) {
        this.registerServerbound(id, packet, serializer);
        this.registerClientbound(id, packet, serializer);
    }

    /**
     * Registers a packet to this protocol as both serverbound and clientbound.
     *
     * @param definition The packet definition.
     * @throws IllegalArgumentException If the packet fails a test creation when being registered as serverbound.
     */
    public final void register(PacketDefinition<? extends Packet, ?> definition) {
        this.registerServerbound(definition);
        this.registerClientbound(definition);
    }

    /**
     * Registers a serverbound packet to this protocol.
     *
     * @param id         Id to register the packet to.
     * @param packet     Packet to register.
     * @param serializer The packet serializer.
     * @throws IllegalArgumentException If the packet fails a test creation.
     */
    public final <T extends Packet, H extends PacketCodecHelper> void registerServerbound(int id, Class<T> packet, PacketSerializer<T, H> serializer) {
        this.registerServerbound(new PacketDefinition<>(id, packet, serializer));
    }

    /**
     * Registers a serverbound packet to this protocol.
     *
     * @param definition The packet definition.
     */
    public final void registerServerbound(PacketDefinition<? extends Packet, ?> definition) {
        this.serverbound.put(definition.getId(), definition);
        this.serverboundIds.put(definition.getPacketClass(), definition.getId());
    }

    /**
     * Registers a clientbound packet to this protocol.
     *
     * @param id         Id to register the packet to.
     * @param packet     Packet to register.
     * @param serializer The packet serializer.
     */
    public final <T extends Packet, H extends PacketCodecHelper> void registerClientbound(int id, Class<T> packet, PacketSerializer<T, H> serializer) {
        this.registerClientbound(new PacketDefinition<>(id, packet, serializer));
    }

    /**
     * Registers a clientbound packet to this protocol.
     *
     * @param definition The packet definition.
     */
    public final void registerClientbound(PacketDefinition<? extends Packet, ?> definition) {
        this.clientbound.put(definition.getId(), definition);
        this.clientboundIds.put(definition.getPacketClass(), definition.getId());
    }

    /**
     * Creates a new instance of a clientbound packet with the given id and read the clientbound input.
     *
     * @param id          Id of the packet to create.
     * @param buf         The buffer to read the packet from.
     * @param codecHelper The codec helper.
     * @return The created packet.
     * @throws IOException if there was an IO error whilst reading the packet.
     * @throws IllegalArgumentException If the packet ID is not registered.
     */
    @SuppressWarnings("unchecked")
    public <H extends PacketCodecHelper> Packet createClientboundPacket(int id, ByteBuf buf, H codecHelper) throws IOException {
        PacketDefinition<?, H> definition = (PacketDefinition<?, H>) this.clientbound.get(id);
        if (definition == null) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }

        return definition.newInstance(buf, codecHelper);
    }

    /**
     * Gets the registered id of a clientbound packet class.
     *
     * @param packetClass Class of the packet to get the id for.
     * @return The packet's registered id.
     * @throws IllegalArgumentException If the packet is not registered.
     */
    public int getClientboundId(Class<? extends Packet> packetClass) {
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
    public int getClientboundId(Packet packet) {
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
    public Class<? extends Packet> getClientboundClass(int id) {
        PacketDefinition<?, ?> definition = this.clientbound.get(id);
        if (definition == null) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }

        return definition.getPacketClass();
    }

    /**
     * Creates a new instance of a serverbound packet with the given id and read the serverbound input.
     *
     * @param id          Id of the packet to create.
     * @param buf         The buffer to read the packet from.
     * @param codecHelper The codec helper.
     * @return The created packet.
     * @throws IOException if there was an IO error whilst reading the packet.
     * @throws IllegalArgumentException If the packet ID is not registered.
     */
    @SuppressWarnings("unchecked")
    public <H extends PacketCodecHelper> Packet createServerboundPacket(int id, ByteBuf buf, H codecHelper) throws IOException {
        PacketDefinition<?, H> definition = (PacketDefinition<?, H>) this.serverbound.get(id);
        if (definition == null) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }

        return definition.newInstance(buf, codecHelper);
    }

    /**
     * Gets the registered id of a serverbound packet class.
     *
     * @param packetClass Class of the packet to get the id for.
     * @return The packet's registered id.
     * @throws IllegalArgumentException If the packet is not registered.
     */
    public int getServerboundId(Class<? extends Packet> packetClass) {
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
    public int getServerboundId(Packet packet) {
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
    public Class<? extends Packet> getServerboundClass(int id) {
        PacketDefinition<?, ?> definition = this.serverbound.get(id);
        if (definition == null) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }

        return definition.getPacketClass();
    }

    /**
     * Gets the serverbound packet definition for the given packet id.
     *
     * @param id The packet id.
     * @return The registered packet's class
     */
    public PacketDefinition<?, ?> getServerboundDefinition(int id) {
        PacketDefinition<?, ?> definition = this.serverbound.get(id);
        if (definition == null) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }

        return definition;
    }

    /**
     * Gets the clientbound packet definition for the given packet id.
     *
     * @param id The packet id.
     * @return The registered packet's class
     */
    public PacketDefinition<?, ?> getClientboundDefinition(int id) {
        PacketDefinition<?, ?> definition = this.clientbound.get(id);
        if (definition == null) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }

        return definition;
    }
}
