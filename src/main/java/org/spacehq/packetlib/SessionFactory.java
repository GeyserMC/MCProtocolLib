package org.spacehq.packetlib;

/**
 * A factory for creating sessions.
 */
public interface SessionFactory {

	/**
	 * Creates a client session.
	 *
	 * @param host Host to connect to.
	 * @param port Port to connect to.
	 * @return The created session.
	 */
	public Session createClientSession(Client client);

	/**
	 * Initializes the factory for creating server sessions.
	 *
	 * @param server Server to initialize for.
	 */
	public ConnectionListener createServerListener(Server server);

}
