package com.github.steveice10.packetlib.event.session;

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
