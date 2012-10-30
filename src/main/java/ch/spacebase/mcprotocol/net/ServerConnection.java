package ch.spacebase.mcprotocol.net;

import java.net.InetSocketAddress;
import java.net.Socket;
import ch.spacebase.mcprotocol.exception.ConnectException;

public class ServerConnection extends Connection {
	
	private String username;
	
	private Socket sock;
	private Server server;
	
	private long aliveTime = System.currentTimeMillis();
	
	public ServerConnection(Protocol prot, Server server, Socket sock) {
		super(prot, sock.getInetAddress().getHostName(), ((InetSocketAddress) sock.getRemoteSocketAddress()).getPort());
		this.server = server;
		this.sock = sock;
	}
	
	public ServerConnection connect() throws ConnectException {
		super.connect(this.sock);
		new KeepAliveThread().start();
		return this;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public Server getServer() {
		return this.server;
	}
	
	private class KeepAliveThread extends Thread {
		@Override
		public void run() {
			while(isConnected()) {
				if(System.currentTimeMillis() - aliveTime > 1000) {
					aliveTime = System.currentTimeMillis();
					getProtocol().keepAlive(ServerConnection.this);
				}
			}
		}
	}
	
}
