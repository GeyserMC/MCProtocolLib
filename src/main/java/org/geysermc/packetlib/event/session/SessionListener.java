package org.geysermc.packetlib.event.session;

import org.geysermc.packetlib.Session;
import org.geysermc.packetlib.packet.Packet;

/**
 * A listener for listening to session events.
 */
public interface SessionListener {
    /**
     * Called when a session receives a packet.
     *
     * @param packet the packet that was just received.
     */
    void packetReceived(Session session, Packet packet);

    /**
     * Called when a session is sending a packet.
     *
     * @param event Data relating to the event.
     */
    void packetSending(PacketSendingEvent event);

    /**
     * Called when a session sends a packet.
     *
     * @param packet Packet just sent.
     */
    void packetSent(Session session, Packet packet);

    /**
     * Called when a session encounters an error while reading or writing packet data.
     *
     * @param event Data relating to the event.
     */
    void packetError(PacketErrorEvent event);

    /**
     * Called when a session connects.
     *
     * @param event Data relating to the event.
     */
    void connected(ConnectedEvent event);

    /**
     * Called when a session is about to disconnect.
     *
     * @param event Data relating to the event.
     */
    void disconnecting(DisconnectingEvent event);

    /**
     * Called when a session is disconnected.
     *
     * @param event Data relating to the event.
     */
    void disconnected(DisconnectedEvent event);
}
