package ch.spacebase.packetlib.event.server;

import ch.spacebase.packetlib.Server;

/**
 * Called when the server is about to close.
 */
public class ServerClosingEvent implements ServerEvent {

	private Server server;
	
	public ServerClosingEvent(Server server) {
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
		listener.serverClosing(this);
	}

}
