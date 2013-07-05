package ch.spacebase.mcprotocol.net;

import java.util.ArrayList;
import java.util.List;

import ch.spacebase.mcprotocol.event.ProtocolEvent;
import ch.spacebase.mcprotocol.event.ServerListener;

/**
 * A server accepting client connections.
 */
public abstract class Server {

	/**
	 * The server's host to listen on.
	 */
	private String host;
	
	/**
	 * The server's port to listen on.
	 */
	private int port;
	
	/**
	 * Whether auth is enabled.
	 */
	private boolean auth;
	
	/**
	 * Listeners listening to this server.
	 */
	private List<ServerListener> listeners = new ArrayList<ServerListener>();
	
	/**
	 * Creates a new server instance.
	 * @param host Host to listen on.
	 * @param port Port to listen on.
	 */
	public Server(String host, int port, boolean auth) {	
		this.host = host;
		this.port = port;
		this.auth = auth;
	}
	
	/**
	 * Gets the host of this server.
	 * @return The server's host.
	 */
	public String getHost() {
		return this.host;
	}

	/**
	 * Gets the port of this server.
	 * @return The server's port.
	 */
	public int getPort() {
		return this.port;
	}
	
	/**
	 * Gets whether authentication is enabled.
	 * @return Whether auth is enabled.
	 */
	public boolean isAuthEnabled() {
		return this.auth;
	}
	
	/**
	 * Adds a listener to listen to this server.
	 * @param listener Listener to add.
	 */
	public void listen(ServerListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Calls an event for this server.
	 * @param event Event to call.
	 * @return The event called.
	 */
	public <T extends ProtocolEvent<ServerListener>> T call(T event) {
		for(ServerListener listener : this.listeners) {
			event.call(listener);
		}

		return event;
	}
	
	/**
	 * Gets whether the server is active.
	 * @return Whether the server is active.
	 */
	public abstract boolean isActive();
	
	/**
	 * Binds the server to its host and port.
	 */
	public abstract void bind();
	
	/**
	 * Shuts the server down.
	 */
	public abstract void shutdown();
	
	/**
	 * Gets a list of connections connected to this server.
	 * @return The server's connections.
	 */
	public abstract List<ServerConnection> getConnections();
	
}
