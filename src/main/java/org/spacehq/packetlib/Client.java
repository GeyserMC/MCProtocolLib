package org.spacehq.packetlib;

import org.spacehq.packetlib.packet.PacketProtocol;

/**
 * A client that may connect to a server.
 */
public class Client implements ConnectTimeoutHandlerContainer {

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
	 * Gets the connect timeout for this client in seconds.
	 *
	 * @return The client's connect timeout.
	 */
	public int getConnectTimeout() {
		return this.timeout;
	}

	/**
	 * Sets the connect timeout for this client in seconds.
	 *
	 * @param timeout Connect timeout to set in seconds.
	 */
	public void setConnectTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * Gets the connect timeout handler for this client.
	 *
	 * @return The client's connect timeout handler.
	 */
	public TimeoutHandler getConnectTimeoutHandler() {
		return this.timeoutHandler;
	}

	/**
	 * Sets the connect timeout handler for this client. Note: Takes effect on connect() call.
	 *
	 * @param timeoutHandler Connect timeout handler to set.
	 */
	public void setConnectTimeoutHandler(TimeoutHandler timeoutHandler) {
		this.timeoutHandler = timeoutHandler;
	}

}
