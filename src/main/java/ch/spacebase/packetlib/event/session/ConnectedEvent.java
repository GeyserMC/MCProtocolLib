package ch.spacebase.packetlib.event.session;

import ch.spacebase.packetlib.Session;

/**
 * Called when the session connects.
 */
public class ConnectedEvent implements SessionEvent {

	private Session session;
	
	public ConnectedEvent(Session session) {
		this.session = session;
	}
	
	/**
	 * Gets the session involved in this event.
	 * @return The event's session.
	 */
	public Session getSession() {
		return this.session;
	}
	
	@Override
	public void call(SessionListener listener) {
		listener.connected(this);
	}

}
