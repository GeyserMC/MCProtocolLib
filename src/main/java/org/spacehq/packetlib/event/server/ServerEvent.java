package org.spacehq.packetlib.event.server;

/**
 * An event relating to servers.
 */
public interface ServerEvent {
    /**
     * Calls the event.
     *
     * @param listener Listener to call the event on.
     */
    public void call(ServerListener listener);
}
