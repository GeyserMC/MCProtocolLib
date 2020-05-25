package com.github.steveice10.packetlib.packet;

import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.crypt.PacketEncryption;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * A protocol for packet sending and receiving.
 * All implementations must have a no-params constructor for server protocol creation.
 */
public abstract class PacketProtocol {
    private final Map<Integer, Class<? extends Packet>> incoming = new HashMap<>();
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
     * @param client  The client that the session belongs to.
     * @param session The created session.
     */
    public abstract void newClientSession(Client client, Session session);

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
        this.incoming.put(id, packet);
        try {
            this.createIncomingPacket(id);
        } catch(IllegalStateException e) {
            this.incoming.remove(id);
            throw new IllegalArgumentException(e.getMessage(), e.getCause());
        }
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
     * Creates a new instance of an incoming packet with the given id.
     *
     * @param id Id of the packet to create.
     * @return The created packet.
     * @throws IllegalArgumentException If the packet ID is not registered.
     * @throws IllegalStateException    If the packet does not have a no-params constructor or cannot be instantiated.
     */
    public final Packet createIncomingPacket(int id) {
        Class<? extends Packet> packet = this.incoming.get(id);
        if (packet == null) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }

        try {
            Constructor<? extends Packet> constructor = packet.getDeclaredConstructor();
            if(!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }

            return constructor.newInstance();
        } catch(NoSuchMethodError e) {
            throw new IllegalStateException("Packet \"" + id + ", " + packet.getName() + "\" does not have a no-params constructor for instantiation.");
        } catch(Exception e) {
            throw new IllegalStateException("Failed to instantiate packet \"" + id + ", " + packet.getName() + "\".", e);
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
