package org.geysermc.mcprotocollib.network.event.server;

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
