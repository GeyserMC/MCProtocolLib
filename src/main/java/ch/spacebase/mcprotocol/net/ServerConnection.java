package ch.spacebase.mcprotocol.net;

import java.net.InetSocketAddress;
import java.net.Socket;
import ch.spacebase.mcprotocol.exception.ConnectException;

public class ServerConnection extends Connection {

	/**
	 * The username of the connected client.
	 */
	private String username;

	/**
	 * The socket of the connection.
	 */
	private Socket sock;
	
	/**
	 * The server this connection belongs to.
	 */
	private Server server;

	/**
	 * The last time a keep alive was sent.
	 */
	private long aliveTime = System.currentTimeMillis();

	/**
	 * Creates a new server connection.
	 * @param prot Protocol to use.
	 * @param server Server this connection belongs to.
	 * @param sock Socket of the connection.
	 */
	public ServerConnection(Protocol prot, Server server, Socket sock) {
		super(prot, sock.getInetAddress().getHostName(), ((InetSocketAddress) sock.getRemoteSocketAddress()).getPort());
		this.server = server;
		this.sock = sock;
	}

	@Override
	public ServerConnection connect() throws ConnectException {
		super.connect(this.sock);
		new KeepAliveThread().start();
		return this;
	}

	/**
	 * Gets the known username of the connected client.
	 * @return The client's username.
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Sets the known username of the connected client.
	 * @param username The client's username.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the server this connection belongs to.
	 * @return The connection's server.
	 */
	public Server getServer() {
		return this.server;
	}

	/**
	 * A thread that keeps the connection alive.
	 */
	private class KeepAliveThread extends Thread {
		@Override
		public void run() {
			while(isConnected()) {
				if(System.currentTimeMillis() - aliveTime > 1000) {
					aliveTime = System.currentTimeMillis();
					getProtocol().keepAlive(ServerConnection.this);
				}
				
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
				}
			}
		}
	}

}
