package ch.spacebase.mcprotocol.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import ch.spacebase.mcprotocol.exception.ConnectException;
import ch.spacebase.mcprotocol.exception.LoginException;
import ch.spacebase.mcprotocol.exception.OutdatedLibraryException;
import ch.spacebase.mcprotocol.util.Util;

public class Client extends Connection {

	private String username;
	private String sessionId;

	public Client(Protocol prot, String host, int port) {
		super(prot, host, port);
	}

	/**
	 * Assigns username without logging into minecraft.net. Use this login
	 * method together with online-mode=false in server.properties.
	 * 
	 * @param username
	 *            The username to assign.
	 */
	public void setUser(String username) {
		this.username = username;
	}

	public void setSessionId(String id) {
		this.sessionId = id;
	}

	public boolean setUser(String username, String password) throws LoginException, OutdatedLibraryException {
		return this.getProtocol().login(this, username, password);
	}

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

	public String getUsername() {
		return this.username;
	}

	public String getSessionId() {
		return this.sessionId;
	}

}
