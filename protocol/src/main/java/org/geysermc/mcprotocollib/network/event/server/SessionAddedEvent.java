package org.geysermc.mcprotocollib.network.event.server;

import org.geysermc.mcprotocollib.network.Server;
import org.geysermc.mcprotocollib.network.Session;

/**
 * Called when a session is added to the server.
 */
public class SessionAddedEvent implements ServerEvent {
    private final Server server;
    private final Session session;

    /**
     * Creates a new SessionAddedEvent instance.
     *
     * @param server Server the session is being added to.
     * @param session Session being added.
     */
    public SessionAddedEvent(Server server, Session session) {
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
        listener.sessionAdded(this);
    }
}
