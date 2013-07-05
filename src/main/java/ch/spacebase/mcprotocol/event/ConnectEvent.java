package ch.spacebase.mcprotocol.event;

import ch.spacebase.mcprotocol.net.ServerConnection;

/**
 * Called when a client connects to the server.
 */
public class ConnectEvent extends ProtocolEvent<ServerListener> {

	/**
	 * The client's connection.
	 */
	private ServerConnection conn;

	/**
	 * Creates a connect event instance.
	 * @param conn Connection of the connecting client.
	 */
	public ConnectEvent(ServerConnection conn) {
		this.conn = conn;
	}

	/**
	 * Gets the client's connection.
	 * @return The client's connection.
	 */
	public ServerConnection getConnection() {
		return this.conn;
	}

	@Override
	public void call(ServerListener listener) {
		listener.onConnect(this);
	}

}
