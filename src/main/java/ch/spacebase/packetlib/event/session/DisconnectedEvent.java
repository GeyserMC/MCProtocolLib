package ch.spacebase.packetlib.event.session;

import ch.spacebase.packetlib.Session;

/**
 * Called when the session is disconnected.
 */
public class DisconnectedEvent implements SessionEvent {

	private Session session;
	private String reason;
	
	public DisconnectedEvent(Session session, String reason) {
		this.session = session;
		this.reason = reason;
	}
	
	/**
	 * Gets the session involved in this event.
	 * @return The event's session.
	 */
	public Session getSession() {
		return this.session;
	}
	
	/**
	 * Gets the reason given for the session disconnecting.
	 * @return The event's reason.
	 */
	public String getReason() {
		return this.reason;
	}
	
	@Override
	public void call(SessionListener listener) {
		listener.disconnected(this);
	}

}
