package com.github.steveice10.packetlib.event.session;

import com.github.steveice10.packetlib.Session;

/**
 * Called when the session connects.
 *
 * @param session The session involved in this event.
 * @see SessionListener#connected(ConnectedEvent)
 */
public record ConnectedEvent(Session session) implements SessionEvent {
    @Override
    public void call(SessionListener listener) {
        listener.connected(this);
    }
}
