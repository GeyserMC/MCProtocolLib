package ch.spacebase.mcprotocol.net;

import ch.spacebase.mcprotocol.event.ProtocolEvent;
import ch.spacebase.mcprotocol.event.ProtocolListener;
import ch.spacebase.mcprotocol.exception.ConnectException;
import ch.spacebase.mcprotocol.packet.Packet;

/**
 * A network connection.
 */
public interface Connection {

	/**
	 * Gets the protocol type of this connection.
	 * @return The connection's protocol type.
	 */
	public abstract PacketRegistry getPacketRegistry();
	
	/**
	 * Gets the remote host of this connection.
	 * @return The connection's remote host.
	 */
	public String getRemoteHost();

	/**
	 * Gets the remote port of this connection.
	 * @return The connection's remote port.
	 */
	public int getRemotePort();
	
	/**
	 * Adds a listener to listen to this connection.
	 * @param listener Listener to add.
	 */
	public void listen(ProtocolListener listener);

	/**
	 * Calls an event for this connection.
	 * @param event Event to call.
	 * @return The event called.
	 */
	public <T extends ProtocolEvent<ProtocolListener>> T call(T event);
	
	/**
	 * Gets the connection's username.
	 * @return The connection's username.
	 */
	public String getUsername();
	
	/**
	 * Sets the connection's username if it isn't already set, ignoring any authentication.
	 * @param name The new username.
	 */
	public void setUsername(String name);
	
	/**
	 * Gets whether the connection is connected.
	 * @return True if the connection is connected.
	 */
	public boolean isConnected();
	
	/**
	 * Connects this connection.
	 * @throws ConnectException If a connection error occurs.
	 */
	public void connect() throws ConnectException;
	
	/**
	 * Disconnects this connection, sending a packet with a reason.
	 * @param reason Reason to disconnect.
	 */
	public void disconnect(String reason);
	
	/**
	 * Disconnects this connection, sending a packet with a reason.
	 * @param reason Reason to disconnect.
	 * @param packet Whether a disconnect packet is necessary.
	 */
	public void disconnect(String reason, boolean packet);
	
	/**
	 * Sends a packet.
	 * @param packet Packet to send.
	 */
	public void send(Packet packet);
	
}
