package org.spacehq.packetlib;

/**
 * A factory for creating sessions.
 */
public interface SessionFactory {
    /**
     * Creates a client session.
     *
     * @param client Client to create the session for.
     * @return The created session.
     */
    public Session createClientSession(Client client);

    /**
     * Creates a server network listener.
     *
     * @param server Server to create the listener for.
     * @return The created listener.
     */
    public ConnectionListener createServerListener(Server server);
}
