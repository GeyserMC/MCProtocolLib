package com.github.steveice10.packetlib.event.session;

import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.packet.Packet;

/**
 * Called when the session is sending a packet.
 */
public class PacketSendingEvent implements SessionEvent {
    private Session session;
    private Packet packet;
    private boolean cancelled = false;

    /**
     * Creates a new PacketSendingEvent instance.
     *
     * @param session Session sending the packet.
     * @param packet  Packet being sent.
     */
    public PacketSendingEvent(Session session, Packet packet) {
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

    /**
     * Sets the packet that should be sent as a result of this event.
     *
     * @param packet The packet to send.
     */
    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    /**
     * Gets whether the event has been cancelled.
     *
     * @return Whether the event has been cancelled.
     */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Sets whether the event should be cancelled.
     *
     * @param cancelled Whether the event should be cancelled.
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public void call(SessionListener listener) {
        listener.packetSending(this);
    }
}
