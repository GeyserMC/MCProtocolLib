package ch.spacebase.mcprotocol.event;

import ch.spacebase.mcprotocol.net.Connection;

/**
 * Called when the connection is disconnected.
 */
public class DisconnectEvent extends ProtocolEvent<ProtocolListener> {

	/**
	 * The connection that was disconnected.
	 */
	private Connection conn;
	
	/**
	 * The given reason for disconnecting.
	 */
	private String reason;
	
	/**
	 * Creates a new disconnect event instance.
	 * @param conn Connection that is disconnecting.
	 * @param reason Reason given for disconnecting.
	 */
	public DisconnectEvent(Connection conn, String reason) {
		this.conn = conn;
		this.reason = reason;
	}

	/**
	 * Gets the connection that is disconnecting.
	 * @return The disconnecting connection.
	 */
	public Connection getConnection() {
		return this.conn;
	}
	
	/**
	 * Gets the given reason for disconnecting.
	 * @return The reason for disconnecting.
	 */
	public String getReason() {
		return this.reason;
	}
	
	@Override
	public void call(ProtocolListener listener) {
		listener.onDisconnect(this);
	}

}
