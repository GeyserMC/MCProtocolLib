package ch.spacebase.mcprotocol.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ch.spacebase.mcprotocol.event.ConnectEvent;
import ch.spacebase.mcprotocol.event.ProtocolEvent;
import ch.spacebase.mcprotocol.event.ServerListener;
import ch.spacebase.mcprotocol.util.Util;

/**
 * A server for clients to connect to.
 */
public class Server {

	/**
	 * The port to listen on.
	 */
	private int port;
	
	/**
	 * Whether the server is online.
	 */
	private boolean online = true;

	/**
	 * The connections connected to the server.
	 */
	private List<ServerConnection> connections = new CopyOnWriteArrayList<ServerConnection>();
	
	/**
	 * The server's key pair.
	 */
	private KeyPair keys;
	
	/**
	 * Whether to verify users.
	 */
	private boolean verify;
	
	/**
	 * The server's protocol class.
	 */
	private Class<? extends Protocol> protocol;
	
	/**
	 * The server's event listeners.
	 */
	private List<ServerListener> listeners = new ArrayList<ServerListener>();

	/**
	 * Creates a new server instance.
	 * @param prot Protocol to use.
	 * @param port Port to listen on.
	 * @param verifyUsers Whether to verify users.
	 */
	public Server(Class<? extends Protocol> prot, int port, boolean verifyUsers) {
		this.port = port;
		this.verify = verifyUsers;
		this.protocol = prot;

		try {
			KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
			gen.initialize(1024);
			this.keys = gen.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Binds the server for listening.
	 */
	public void bind() {
		try {
			ServerSocket sock = new ServerSocket(this.port);
			new ServerConnectionThread(sock).start();
		} catch (IOException e) {
			Util.logger().severe("Failed to bind to port " + this.port + "!");
			e.printStackTrace();
		}
	}

	/**
	 * Shuts down the server.
	 */
	public void shutdown() {
		for(ServerConnection conn : this.connections) {
			conn.disconnect("The server is shutting down!");
		}

		this.online = false;
	}
	
	/**
	 * Adds a listener to the server.
	 * @param listener Listener to add.
	 */
	public void listen(ServerListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Calls an event on the server.
	 * @param event The event to call.
	 * @return The called event.
	 */
	public <T extends ProtocolEvent<ServerListener>> T call(T event) {
		for(ServerListener listener : this.listeners) {
			event.call(listener);
		}

		return event;
	}

	/**
	 * Gets the server's key pair.
	 * @return The server's key pair.
	 */
	public KeyPair getKeys() {
		return this.keys;
	}

	/**
	 * Gets whether connected users need to be verified.
	 * @return Whether to verify users.
	 */
	public boolean verifyUsers() {
		return this.verify;
	}

	/**
	 * Listens for new server connections.
	 */
	private class ServerConnectionThread extends Thread {
		/**
		 * The server's socket.
		 */
		private ServerSocket sock;

		/**
		 * Creates a new server connection thread instance.
		 * @param sock Socket to listen on.
		 */
		public ServerConnectionThread(ServerSocket sock) {
			this.sock = sock;
		}

		@Override
		public void run() {
			while(online) {
				try {
					Socket client = this.sock.accept();
					try {
						ServerConnection conn = new ServerConnection(Server.this.protocol.getDeclaredConstructor().newInstance(), Server.this, client).connect();
						connections.add(conn);
						call(new ConnectEvent(conn));
					} catch (Exception e) {
						Util.logger().severe("Failed to create server connection!");
						e.printStackTrace();
					}
				} catch (IOException e) {
					Util.logger().severe("Failed to accept connection from client!");
					e.printStackTrace();
					continue;
				}
				
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	/**
	 * Gets the server's connections.
	 * @return The server's connections.
	 */
	public List<ServerConnection> getConnections() {
		return new ArrayList<ServerConnection>(this.connections);
	}

}
