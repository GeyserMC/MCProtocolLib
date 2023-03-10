package com.github.steveice10.packetlib.event.server;

/**
 * A listener for listening to server events.
 */
public interface ServerListener {
    /**
     * Called when a server is bound to its host and port.
     *
     * @param event Data relating to the event.
     */
    public void serverBound(ServerBoundEvent event);

    /**
     * Called when a server is about to close.
     *
     * @param event Data relating to the event.
     */
    public void serverClosing(ServerClosingEvent event);

    /**
     * Called when a server is closed.
     *
     * @param event Data relating to the event.
     */
    public void serverClosed(ServerClosedEvent event);

    /**
     * Called when a session is added to the server.
     *
     * @param event Data relating to the event.
     */
    public void sessionAdded(SessionAddedEvent event);

    /**
     * Called when a session is removed and disconnected from the server.
     *
     * @param event Data relating to the event.
     */
    public void sessionRemoved(SessionRemovedEvent event);
}
