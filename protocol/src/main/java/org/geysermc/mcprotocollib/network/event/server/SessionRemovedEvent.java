package org.geysermc.mcprotocollib.network.event.server;

import org.geysermc.mcprotocollib.network.Server;
import org.geysermc.mcprotocollib.network.Session;

/**
 * Called when a session is removed and disconnected from the server.
 */
public class SessionRemovedEvent implements ServerEvent {
    private final Server server;
    private final Session session;

    /**
     * Creates a new SessionRemovedEvent instance.
     *
     * @param server Server the session is being removed from.
     * @param session Session being removed.
     */
    public SessionRemovedEvent(Server server, Session session) {
        this.server = server;
        this.session = session;
    }

    /**
     * Gets the server involved in this event.
     *
     * @return The event's server.
     */
    public Server getServer() {
        return this.server;
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
    public void call(ServerListener listener) {
        listener.sessionRemoved(this);
    }
}
