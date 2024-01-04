package com.github.steveice10.packetlib.event.server;

/**
 * An event relating to servers.
 */
public interface ServerEvent {
    /**
     * Calls the event.
     *
     * @param listener Listener to call the event on.
     */
    void call(ServerListener listener);
}
