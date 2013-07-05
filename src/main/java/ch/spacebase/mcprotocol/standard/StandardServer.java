package ch.spacebase.mcprotocol.standard;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ch.spacebase.mcprotocol.event.ConnectEvent;
import ch.spacebase.mcprotocol.net.Server;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.util.Util;

/**
 * A server implementing standard Minecraft protocol.
 */
public class StandardServer extends Server {

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
	 * Creates a new standard server.
	 * @param host Host to listen on.
	 * @param port Port to listen on.
	 * @param auth Whether to use authentication.
	 */
	public StandardServer(String host, int port, boolean auth) {
		super(host, port, auth);
		try {
			KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
			gen.initialize(1024);
			this.keys = gen.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Gets the server's key pair.
	 * @return The server's key pair.
	 */
	public KeyPair getKeys() {
		return this.keys;
	}

	@Override
	public boolean isActive() {
		return this.online;
	}

	@Override
	public void bind() {
		try {
			ServerSocket sock = new ServerSocket();
			sock.bind(new InetSocketAddress(this.getHost(), this.getPort()));
			new ServerConnectionThread(sock).start();
		} catch (IOException e) {
			Util.logger().severe("Failed to bind to " + this.getHost() + ":" + this.getPort() + "!");
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown() {
		for(ServerConnection conn : this.connections) {
			conn.disconnect("The server is shutting down!");
		}

		this.online = false;
	}

	@Override
	public List<ServerConnection> getConnections() {
		return this.connections;
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
						ServerConnection conn = new StandardServerConnection(StandardServer.this, client);
						conn.connect();
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

}
