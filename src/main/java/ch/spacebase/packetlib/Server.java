package ch.spacebase.packetlib;

import java.util.ArrayList;
import java.util.List;

import ch.spacebase.packetlib.event.server.ServerBoundEvent;
import ch.spacebase.packetlib.event.server.ServerClosedEvent;
import ch.spacebase.packetlib.event.server.ServerClosingEvent;
import ch.spacebase.packetlib.event.server.ServerEvent;
import ch.spacebase.packetlib.event.server.ServerListener;
import ch.spacebase.packetlib.event.server.SessionAddedEvent;
import ch.spacebase.packetlib.event.server.SessionRemovedEvent;
import ch.spacebase.packetlib.packet.PacketProtocol;

/**
 * A server that listens on a given host and port.
 */
public class Server {

	private String host;
	private int port;
	private PacketProtocol protocol;
	private SessionFactory factory;
	private ConnectionListener listener;
	private List<Session> sessions = new ArrayList<Session>();
	
	private List<ServerListener> listeners = new ArrayList<ServerListener>();
	
	public Server(String host, int port, PacketProtocol protocol, SessionFactory factory) {
		this.host = host;
		this.port = port;
		this.protocol = protocol;
		this.factory = factory;
		this.protocol.newServer(this);
	}
	
	/**
	 * Binds and initializes the server.
	 * @return The server after being bound.
	 */
	public Server bind() {
		this.listener = this.factory.createServerListener(this);
		this.callEvent(new ServerBoundEvent(this));
		return this;
	}
	
	/**
	 * Gets the host this server is bound to.
	 * @return The server's host.
	 */
	public String getHost() {
		return this.host;
	}

	/**
	 * Gets the port this server is bound to.
	 * @return The server's port.
	 */
	public int getPort() {
		return this.port;
	}
	
	/**
	 * Gets the listeners listening on this session.
	 * @return This session's listeners.
	 */
	public List<ServerListener> getListeners() {
		return new ArrayList<ServerListener>(this.listeners);
	}
	
	/**
	 * Adds a listener to this session.
	 * @param listener Listener to add.
	 */
	public void addListener(ServerListener listener) {
		this.listeners.add(listener);
	}
	
	/**
	 * Removes a listener from this session.
	 * @param listener Listener to remove.
	 */
	public void removeListener(ServerListener listener) {
		this.listeners.remove(listener);
	}
	
	/**
	 * Calls an event on the listeners of this session.
	 * @param event Event to call.
	 */
	public void callEvent(ServerEvent event) {
		for(ServerListener listener : this.listeners) {
			event.call(listener);
		}
	}
	
	/**
	 * Gets the packet protocol of the server.
	 * @return The server's packet protocol.
	 */
	public PacketProtocol getPacketProtocol() {
		return this.protocol;
	}
	
	/**
	 * Gets all sessions belonging to this server.
	 * @return Sessions belonging to this server.
	 */
	public List<Session> getSessions() {
		return new ArrayList<Session>(this.sessions);
	}
	
	/**
	 * Adds the given session to this server.
	 * @param session Session to add.
	 */
	public void addSession(Session session) {
		this.sessions.add(session);
		this.callEvent(new SessionAddedEvent(this, session));
	}
	
	/**
	 * Removes the given session from this server.
	 * @param session Session to remove.
	 */
	public void removeSession(Session session) {
		this.sessions.remove(session);
		if(session.isConnected()) {
			session.disconnect("Connection closed.");
		}
		
		this.callEvent(new SessionRemovedEvent(this, session));
	}
	
	/**
	 * Gets whether the server is listening.
	 * @return Whether the server is listening.
	 */
	public boolean isListening() {
		return this.listener.isListening();
	}
	
	/**
	 * Closes the server.
	 */
	public void close() {
		// Close server in a separate thread to prevent stalling from closing a server inside an event or packet handling.
		new Thread(new Runnable() {
			@Override
			public void run() {
				callEvent(new ServerClosingEvent(Server.this));
				for(Session session : getSessions()) {
					if(session.isConnected()) {
						session.disconnect("Server closed.");
					}
				}
				
				listener.close();
				callEvent(new ServerClosedEvent(Server.this));
			}
		}, "CloseServer").start();
	}

}
