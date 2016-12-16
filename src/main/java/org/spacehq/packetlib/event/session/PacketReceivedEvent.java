package org.spacehq.packetlib.event.session;

import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.packet.Packet;

/**
 * Called when the session receives a packet.
 */
public class PacketReceivedEvent implements SessionEvent {
    private Session session;
    private Packet packet;

    /**
     * Creates a new PacketReceivedEvent instance.
     *
     * @param session Session receiving a packet.
     * @param packet  Packet being received.
     */
    public PacketReceivedEvent(Session session, Packet packet) {
        this.session = session;
        this.packet = packet;
    }

    /**
     * Gets the session involved in this event.
     *
     * @return The event's session.
     */
    public Session getSession() {
        return this.session;
    }

    /**
     * Gets the packet involved in this event as the required type.
     *
     * @param <T> Type of the packet.
     * @return The event's packet as the required type.
     * @throws IllegalStateException If the packet's value isn't of the required type.
     */
    @SuppressWarnings("unchecked")
    public <T extends Packet> T getPacket() {
        try {
            return (T) this.packet;
        } catch(ClassCastException e) {
            throw new IllegalStateException("Tried to get packet as the wrong type. Actual type: " + this.packet.getClass().getName());
        }
    }

    @Override
    public void call(SessionListener listener) {
        listener.packetReceived(this);
    }
}
