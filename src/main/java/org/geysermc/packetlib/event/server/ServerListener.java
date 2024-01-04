package org.geysermc.packetlib.event.server;

/**
 * A listener for listening to server events.
 */
public interface ServerListener {
    /**
     * Called when a server is bound to its host and port.
     *
     * @param event Data relating to the event.
     */
    void serverBound(ServerBoundEvent event);

    /**
     * Called when a server is about to close.
     *
     * @param event Data relating to the event.
     */
    void serverClosing(ServerClosingEvent event);

    /**
     * Called when a server is closed.
     *
     * @param event Data relating to the event.
     */
    void serverClosed(ServerClosedEvent event);

    /**
     * Called when a session is added to the server.
     *
     * @param event Data relating to the event.
     */
    void sessionAdded(SessionAddedEvent event);

    /**
     * Called when a session is removed and disconnected from the server.
     *
     * @param event Data relating to the event.
     */
    void sessionRemoved(SessionRemovedEvent event);
}
