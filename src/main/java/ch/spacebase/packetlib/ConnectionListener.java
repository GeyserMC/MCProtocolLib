package ch.spacebase.packetlib;

import ch.spacebase.packetlib.packet.PacketProtocol;

/**
 * A listener for new sessions on a host and port.
 */
public interface ConnectionListener {

	/**
	 * Gets the host the session is listening on.
	 * @return The listening host.
	 */
	public String getHost();
	
	/**
	 * Gets the port the session is listening on.
	 * @return The listening port.
	 */
	public int getPort();
	
	/**
	 * Gets the packet protocol of the listener.
	 * @return The listener's packet protocol.
	 */
	public PacketProtocol getPacketProtocol();
	
	/**
	 * Returns true if the listener is listening.
	 * @return True if the listener is listening.
	 */
	public boolean isListening();
	
	/**
	 * Closes the listener.
	 */
	public void close();
	
}
