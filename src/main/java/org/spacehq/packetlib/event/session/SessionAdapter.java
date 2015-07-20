package org.spacehq.packetlib.event.session;

/**
 * An adapter for picking session events to listen for.
 */
public class SessionAdapter implements SessionListener {
    @Override
    public void packetReceived(PacketReceivedEvent event) {
    }

    @Override
    public void packetSent(PacketSentEvent event) {
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
