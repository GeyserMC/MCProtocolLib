package org.geysermc.mcprotocollib.network.event.session;

/**
 * An event relating to sessions.
 */
public interface SessionEvent {
    /**
     * Calls the event.
     *
     * @param listener Listener to call the event on.
     */
    void call(SessionListener listener);
}
