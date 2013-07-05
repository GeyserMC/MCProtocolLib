package ch.spacebase.mcprotocol.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import ch.spacebase.mcprotocol.exception.ConnectException;
import ch.spacebase.mcprotocol.exception.LoginException;
import ch.spacebase.mcprotocol.exception.OutdatedLibraryException;
import ch.spacebase.mcprotocol.util.Util;

/**
 * A client connection.
 */
public class Client extends Connection {

	/**
	 * The client's username.
	 */
	private String username;
	
	/**
	 * The client's session id.
	 */
	private String sessionId;

	/**
	 * Creates a new client instance.
	 * @param prot Protocol to use.
	 * @param host Host to connect to.
	 * @param port Port to connect to.
	 */
	public Client(Protocol prot, String host, int port) {
		super(prot, host, port);
	}

	/**
	 * Gets the client's username.
	 * @return The client's username.
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Gets the client's password.
	 * @return The client's password.
	 */
	public String getSessionId() {
		return this.sessionId;
	}
	
	/**
	 * Sets the username without logging in.
	 * NOTICE: The server must have offline-mode enabled to be able to login.
	 * @param username The username to use.
	 */
	public void setUser(String username) {
		this.username = username;
	}

	/**
	 * Sets the client's session id.
	 * @param id The new session id.
	 */
	public void setSessionId(String id) {
		this.sessionId = id;
	}

	/**
	 * Sets the user of the client and attempts to log in.
	 * @param username Username to use.
	 * @param password Password to use.
	 * @return Whether the login was successful.
	 * @throws LoginException If a login error occured.
	 * @throws OutdatedLibraryException If the library is outdated.
	 */
	public boolean setUser(String username, String password) throws LoginException, OutdatedLibraryException {
		return this.getProtocol().login(this, username, password);
	}
	
	@Override
	public Client connect() throws ConnectException {
		Util.logger().info("Connecting to \"" + this.host + ":" + this.port + "\"...");

		try {
			Socket sock = new Socket(InetAddress.getByName(this.host), this.port);
			sock.setSoTimeout(30000);
			sock.setTrafficClass(24);
			super.connect(sock);
		} catch (UnknownHostException e) {
			throw new ConnectException("Unknown host: " + this.host);
		} catch (IOException e) {
			throw new ConnectException("Failed to open stream: " + this.host, e);
		}

		this.protocol.connect(this);
		return this;
	}

}
