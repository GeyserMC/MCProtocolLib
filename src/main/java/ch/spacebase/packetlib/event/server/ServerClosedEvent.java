package ch.spacebase.packetlib.event.server;

import ch.spacebase.packetlib.Server;

/**
 * Called when the server is closed.
 */
public class ServerClosedEvent implements ServerEvent {

	private Server server;
	
	public ServerClosedEvent(Server server) {
		this.server = server;
	}
	
	/**
	 * Gets the server involved in this event.
	 * @return The event's server.
	 */
	public Server getServer() {
		return this.server;
	}
	
	@Override
	public void call(ServerListener listener) {
		listener.serverClosed(this);
	}

}
