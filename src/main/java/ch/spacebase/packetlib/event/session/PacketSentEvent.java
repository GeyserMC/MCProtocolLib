package ch.spacebase.packetlib.event.session;

import ch.spacebase.packetlib.Session;
import ch.spacebase.packetlib.packet.Packet;

/**
 * Called when the session has sent a packet.
 */
public class PacketSentEvent implements SessionEvent {

	private Session session;
	private Packet packet;
	
	public PacketSentEvent(Session session, Packet packet) {
		this.session = session;
		this.packet = packet;
	}
	
	/**
	 * Gets the session involved in this event.
	 * @return The event's session.
	 */
	public Session getSession() {
		return this.session;
	}
	
	/**
	 * Gets the packet involved in this event.
	 * @return The event's packet.
	 */
	public Packet getPacket() {
		return this.packet;
	}
	
	@Override
	public void call(SessionListener listener) {
		listener.packetSent(this);
	}

}
