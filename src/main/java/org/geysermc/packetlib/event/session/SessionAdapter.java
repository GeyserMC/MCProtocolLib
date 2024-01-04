package org.geysermc.packetlib.event.session;

import org.geysermc.packetlib.Session;
import org.geysermc.packetlib.packet.Packet;

/**
 * An adapter for picking session events to listen for.
 */
public class SessionAdapter implements SessionListener {
    @Override
    public void packetReceived(Session session, Packet packet) {
    }

    @Override
    public void packetSending(PacketSendingEvent event) {
    }

    @Override
    public void packetSent(Session session, Packet packet) {
    }

    @Override
    public void packetError(PacketErrorEvent event) {
    }

    @Override
    public void connected(ConnectedEvent event) {
    }

    @Override
    public void disconnecting(DisconnectingEvent event) {
    }

    @Override
    public void disconnected(DisconnectedEvent event) {
    }
}
