package org.spacehq.packetlib;

import org.spacehq.packetlib.packet.PacketProtocol;

/**
 * A client that may connect to a server.
 */
public class Client {

	private String host;
	private int port;
	private PacketProtocol protocol;
	private Session session;
	private int timeout = 30;
	private TimeoutHandler timeoutHandler;

	public Client(String host, int port, PacketProtocol protocol, SessionFactory factory) {
		this.host = host;
		this.port = port;
		this.protocol = protocol;
		this.session = factory.createClientSession(this);
	}

	/**
	 * Gets the host the client is connecting to.
	 *
	 * @return The host the client is connecting to.
	 */
	public String getHost() {
		return this.host;
	}

	/**
	 * Gets the port the client is connecting to.
	 *
	 * @return The port the client is connecting to.
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * Gets the packet protocol of the client.
	 *
	 * @return The client's packet protocol.
	 */
	public PacketProtocol getPacketProtocol() {
		return this.protocol;
	}

	/**
	 * Gets the session of the client.
	 *
	 * @return The client's session.
	 */
	public Session getSession() {
		return this.session;
	}

	/**
	 * Gets the session timeout for this client in seconds.
	 *
	 * @return The client's session timeout.
	 */
	public int getTimeout() {
		return this.timeout;
	}

	/**
	 * Sets the session timeout for this client in seconds. Note: This will only be applied to sessions created after the timeout is set.
	 *
	 * @param timeout Timeout to set in seconds.
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * Gets the session timeout handler for this client.
	 *
	 * @return The client's session timeout handler.
	 */
	public TimeoutHandler getTimeoutHandler() {
		return this.timeoutHandler;
	}

	/**
	 * Sets the session timeout handler for this client. Note: This will only be applied to sessions created after the timeout is set.
	 *
	 * @param timeout Timeout handler to set.
	 */
	public void setTimeoutHandler(TimeoutHandler timeout) {
		this.timeoutHandler = timeout;
	}

}
