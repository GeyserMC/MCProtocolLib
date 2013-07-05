package ch.spacebase.mcprotocol.standard;

import java.net.InetSocketAddress;
import java.net.Socket;
import ch.spacebase.mcprotocol.exception.ConnectException;
import ch.spacebase.mcprotocol.net.Server;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.standard.packet.PacketKeepAlive;
import ch.spacebase.mcprotocol.util.Util;

/**
 * A server connection for the standard minecraft protocol.
 */
public class StandardServerConnection extends StandardConnection implements ServerConnection {
	
	/**
	 * The server this connection belongs to.
	 */
	private Server server;
	
	/**
	 * The connection's temporary socket variable.
	 */
	private Socket sock;
	
	/**
	 * The last time a keep alive was sent.
	 */
	private long aliveTime = System.currentTimeMillis();
	
	/**
	 * The last sent server keep alive id.
	 */
	private int aliveId;
	
	/**
	 * The protocol's login key.
	 */
	private String loginKey;
	
	/**
	 * The protocol's security token.
	 */
	private byte token[];
	
	/**
	 * Creates a new server connection.
	 * @param server Server the connection belongs to.
	 * @param sock Socket to use.
	 */
	public StandardServerConnection(Server server, Socket sock) {
		super(sock.getInetAddress().getHostName(), ((InetSocketAddress) sock.getRemoteSocketAddress()).getPort());
		this.server = server;
		this.sock = sock;
	}
	
	/**
	 * Gets the last id used in a keep alive to this connection.
	 * @return The last keep alive id.
	 */
	public int getLastAliveId() {
		return this.aliveId;
	}
	
	/**
	 * Gets the last time a keep alive was performed.
	 * @return The last keep alive time.
	 */
	public long getLastAliveTime() {
		return this.aliveTime;
	}

	@Override
	public Server getServer() {
		return this.server;
	}

	@Override
	public void connect() throws ConnectException {
		super.connect(this.sock);
		this.sock = null;
		new KeepAliveThread().start();
	}
	
	/**
	 * Gets the connection's login key.
	 * @return The connection's login key.
	 */
	public String getLoginKey() {
		return this.loginKey;
	}

	/**
	 * Sets the connection's login key.
	 * @param key The new login key.
	 */
	public void setLoginKey(String key) {
		this.loginKey = key;
	}

	/**
	 * Gets the connection's security token.
	 * @return The connection's security token.
	 */
	public byte[] getToken() {
		return this.token;
	}

	/**
	 * Sets the connection's security token.
	 * @param token The new security token.
	 */
	public void setToken(byte token[]) {
		this.token = token;
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
					aliveId = Util.random().nextInt();
					send(new PacketKeepAlive(aliveId));
				}
				
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
}
