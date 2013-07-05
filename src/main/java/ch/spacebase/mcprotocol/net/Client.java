package ch.spacebase.mcprotocol.net;

import ch.spacebase.mcprotocol.exception.LoginException;
import ch.spacebase.mcprotocol.exception.OutdatedLibraryException;

/**
 * A client connected to a server.
 */
public interface Client extends Connection {

	/**
	 * Logs the client in using the given username and password.
	 * @param username Username to login with.
	 * @param password Password to login with.
	 * @return Whether the login was successful.
	 */
	public boolean login(String username, String password) throws LoginException, OutdatedLibraryException;

}
