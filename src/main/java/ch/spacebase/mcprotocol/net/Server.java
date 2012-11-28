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

import ch.spacebase.mcprotocol.event.ProtocolEvent;
import ch.spacebase.mcprotocol.event.ServerListener;
import ch.spacebase.mcprotocol.util.Util;

public class Server {

	private int port;
	private boolean online = true;

	private List<ServerConnection> connections = new CopyOnWriteArrayList<ServerConnection>();
	private KeyPair keys;
	private boolean verify;
	private Class<? extends Protocol> protocol;
	private List<ServerListener> listeners = new ArrayList<ServerListener>();

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

	public void bind() {
		try {
			ServerSocket sock = new ServerSocket(this.port);
			new ServerConnectionThread(sock).start();
		} catch (IOException e) {
			Util.logger().severe("Failed to bind to port " + this.port + "!");
			e.printStackTrace();
		}
	}

	public void shutdown() {
		for(ServerConnection conn : this.connections) {
			conn.disconnect("The server is shutting down!");
		}

		this.online = false;
	}
	
	public void listen(ServerListener listener) {
		this.listeners.add(listener);
	}

	public <T extends ProtocolEvent<ServerListener>> T call(T event) {
		for(ServerListener listener : this.listeners) {
			event.call(listener);
		}

		return event;
	}

	public KeyPair getKeys() {
		return this.keys;
	}

	public boolean verifyUsers() {
		return this.verify;
	}

	private class ServerConnectionThread extends Thread {
		private ServerSocket sock;

		public ServerConnectionThread(ServerSocket sock) {
			this.sock = sock;
		}

		@Override
		public void run() {
			while(online) {
				try {
					Socket client = this.sock.accept();
					try {
						connections.add(new ServerConnection(Server.this.protocol.getDeclaredConstructor().newInstance(), Server.this, client).connect());
					} catch (Exception e) {
						Util.logger().severe("Failed to create server connection!");
						e.printStackTrace();
					}
				} catch (IOException e) {
					Util.logger().severe("Failed to accept connection from client!");
					e.printStackTrace();
					continue;
				}
			}
		}
	}

	public List<ServerConnection> getConnections() {
		return new ArrayList<ServerConnection>(this.connections);
	}

}
