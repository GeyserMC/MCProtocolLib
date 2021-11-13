package com.github.steveice10.packetlib.packet;

import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.crypt.PacketEncryption;
import com.github.steveice10.packetlib.io.NetInput;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A protocol for packet sending and receiving.
 * All implementations must have a no-params constructor for server protocol creation.
 */
public abstract class PacketProtocol {
    private final Map<Integer, Constructor<? extends Packet>> incoming = new HashMap<>();
    private final Map<Class<? extends Packet>, Integer> outgoing = new HashMap<>();

    private final Map<Integer, Class<? extends Packet>> outgoingClasses = new HashMap<>();

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
        this.incoming.clear();
        this.outgoing.clear();
        this.outgoingClasses.clear();
    }

    /**
     * Registers a packet to this protocol as both incoming and outgoing.
     *
     * @param id     Id to register the packet to.
     * @param packet Packet to register.
     * @throws IllegalArgumentException If the packet fails a test creation when being registered as incoming.
     */
    public final void register(int id, Class<? extends Packet> packet) {
        this.registerIncoming(id, packet);
        this.registerOutgoing(id, packet);
    }

    /**
     * Registers an incoming packet to this protocol.
     *
     * @param id     Id to register the packet to.
     * @param packet Packet to register.
     * @throws IllegalArgumentException If the packet fails a test creation.
     */
    public final void registerIncoming(int id, Class<? extends Packet> packet) {
        Constructor<? extends Packet> constructor;
        try {
            constructor = packet.getConstructor(NetInput.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Packet does not have a constructor that takes in NetInput!");
        }
        this.incoming.put(id, constructor);
    }

    /**
     * Registers an outgoing packet to this protocol.
     *
     * @param id     Id to register the packet to.
     * @param packet Packet to register.
     */
    public final void registerOutgoing(int id, Class<? extends Packet> packet) {
        this.outgoing.put(packet, id);
        this.outgoingClasses.put(id, packet);
    }

    /**
     * Creates a new instance of an incoming packet with the given id and read the incoming input.
     *
     * @param id Id of the packet to create.
     * @return The created packet.
     * @throws IOException if there was an IO error whilst reading the packet.
     * @throws IllegalArgumentException If the packet ID is not registered.
     */
    public final Packet createIncomingPacket(int id, NetInput in) throws IOException {
        Constructor<? extends Packet> constructor = this.incoming.get(id);
        if (constructor == null) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }

        try {
            return constructor.newInstance(in);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Packet \"" + id + ", " + constructor.getDeclaringClass().getName() + "\" could not be instantiated.");
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            }
            throw new IllegalStateException("Failed to instantiate packet \"" + id + ", " + constructor.getDeclaringClass().getName() + "\".", e);
        }
    }

    /**
     * Gets the registered id of an outgoing packet class.
     *
     * @param packetClass Class of the packet to get the id for.
     * @return The packet's registered id.
     * @throws IllegalArgumentException If the packet is not registered.
     */
    public final int getOutgoingId(Class<? extends Packet> packetClass) {
        Integer packetId = this.outgoing.get(packetClass);
        if(packetId == null) {
            throw new IllegalArgumentException("Unregistered outgoing packet class: " + packetClass.getName());
        }

        return packetId;
    }

    /**
     * Gets the registered id of an outgoing {@link Packet} instance.
     *
     * @param packet Instance of {@link Packet} to get the id for.
     * @return The packet's registered id.
     * @throws IllegalArgumentException If the packet is not registered.
     */
    public final int getOutgoingId(Packet packet) {
        if(packet instanceof BufferedPacket) {
            return getOutgoingId(((BufferedPacket)packet).getPacketClass());
        }

        return getOutgoingId(packet.getClass());
    }

    /**
     * Gets the packet class for a packet id.
     * @param id The packet id.
     * @return The registered packet's class
     * @throws IllegalArgumentException If the packet ID is not registered.
     */
    public final Class<? extends Packet> getOutgoingClass(int id) {
        Class<? extends Packet> packet = this.outgoingClasses.get(id);
        if(packet == null) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }

        return packet;
    }
}
