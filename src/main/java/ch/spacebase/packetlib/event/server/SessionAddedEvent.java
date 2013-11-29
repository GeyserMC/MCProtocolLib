package ch.spacebase.packetlib.event.server;

import ch.spacebase.packetlib.Server;
import ch.spacebase.packetlib.Session;

/**
 * Called when a session is added to the server.
 */
public class SessionAddedEvent implements ServerEvent {

	private Server server;
	private Session session;
	
	public SessionAddedEvent(Server server, Session session) {
		this.server = server;
		this.session = session;
	}
	
	/**
	 * Gets the server involved in this event.
	 * @return The event's server.
	 */
	public Server getServer() {
		return this.server;
	}
	
	/**
	 * Gets the session involved in this event.
	 * @return The event's session.
	 */
	public Session getSession() {
		return this.session;
	}
	
	@Override
	public void call(ServerListener listener) {
		listener.sessionAdded(this);
	}

}
