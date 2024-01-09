package org.geysermc.mcprotocollib.network.event.session;

import org.geysermc.mcprotocollib.network.Session;

/**
 * Called when the session connects.
 */
public class ConnectedEvent implements SessionEvent {
    private final Session session;

    /**
     * Creates a new ConnectedEvent instance.
     *
     * @param session Session being connected.
     */
    public ConnectedEvent(Session session) {
        this.session = session;
    }

    /**
     * Gets the session involved in this event.
     *
     * @return The event's session.
     */
    public Session getSession() {
        return this.session;
    }

    @Override
    public void call(SessionListener listener) {
        listener.connected(this);
    }
}
