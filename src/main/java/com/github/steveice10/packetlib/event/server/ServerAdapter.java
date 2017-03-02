package com.github.steveice10.packetlib.event.server;

/**
 * An adapter for picking server events to listen for.
 */
public class ServerAdapter implements ServerListener {
    @Override
    public void serverBound(ServerBoundEvent event) {
    }

    @Override
    public void serverClosing(ServerClosingEvent event) {
    }

    @Override
    public void serverClosed(ServerClosedEvent event) {
    }

    @Override
    public void sessionAdded(SessionAddedEvent event) {
    }

    @Override
    public void sessionRemoved(SessionRemovedEvent event) {
    }
}
