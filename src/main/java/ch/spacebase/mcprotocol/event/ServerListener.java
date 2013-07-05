package ch.spacebase.mcprotocol.event;

/**
 * A listener for server-related events.
 */
public abstract class ServerListener {

	/**
	 * Called when a client connects to the server.
	 * @param event The called event.
	 */
	public abstract void onConnect(ConnectEvent event);
	
}
