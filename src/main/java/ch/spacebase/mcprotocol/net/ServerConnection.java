package ch.spacebase.mcprotocol.net;

import ch.spacebase.mcprotocol.net.Server;

/**
 * A server's connection to a client.
 */
public interface ServerConnection extends Connection {

	/**
	 * Gets the server this connection belongs to.
	 * @return The connection's server.
	 */
	public Server getServer();

}
