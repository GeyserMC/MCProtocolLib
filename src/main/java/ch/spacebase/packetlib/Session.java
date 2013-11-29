package ch.spacebase.packetlib;

import java.util.List;

import ch.spacebase.packetlib.event.session.SessionEvent;
import ch.spacebase.packetlib.event.session.SessionListener;
import ch.spacebase.packetlib.packet.Packet;
import ch.spacebase.packetlib.packet.PacketProtocol;

/**
 * A network session.
 */
public interface Session {

	/**
	 * Gets the host the session is connected to.
	 * @return The connected host.
	 */
	public String getHost();
	
	/**
	 * Gets the port the session is connected to.
	 * @return The connected port.
	 */
	public int getPort();
	
	/**
	 * Gets the packet protocol of the session.
	 * @return The session's packet protocol.
	 */
	public PacketProtocol getPacketProtocol();
	
	/**
	 * Gets the listeners listening on this session.
	 * @return This session's listeners.
	 */
	public List<SessionListener> getListeners();
	
	/**
	 * Adds a listener to this session.
	 * @param listener Listener to add.
	 */
	public void addListener(SessionListener listener);
	
	/**
	 * Removes a listener from this session.
	 * @param listener Listener to remove.
	 */
	public void removeListener(SessionListener listener);
	
	/**
	 * Calls an event on the listeners of this session.
	 * @param event Event to call.
	 */
	public void callEvent(SessionEvent event);
	
	/**
	 * Returns true if the session is connected.
	 * @return True if the session is connected.
	 */
	public boolean isConnected();
	
	/**
	 * Sends a packet.
	 * @param packet Packet to send.
	 */
	public void send(Packet packet);
	
	/**
	 * Disconnects the session.
	 * @param reason Reason for disconnecting.
	 */
	public void disconnect(String reason);

}
