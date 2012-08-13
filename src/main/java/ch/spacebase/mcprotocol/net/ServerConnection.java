package ch.spacebase.mcprotocol.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.crypto.SecretKey;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.crypto.io.CipherOutputStream;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import ch.spacebase.mcprotocol.event.PacketRecieveEvent;
import ch.spacebase.mcprotocol.exception.ConnectException;
import ch.spacebase.mcprotocol.net.packet.Packet;
import ch.spacebase.mcprotocol.net.packet.PacketDisconnect;
import ch.spacebase.mcprotocol.util.Util;

public class ServerConnection extends Connection {
	
	private String username;
	
	private DataInputStream input;
	private DataOutputStream output;
	
	private Queue<Packet> packets = new ConcurrentLinkedQueue<Packet>();
	private boolean connected;
	
	private Socket sock;
	private Server server;
	private SecretKey key;
	
	private String loginKey;
	private byte token[];
	
	public ServerConnection(Server server, Socket sock) {
		super(sock.getInetAddress().getHostName(), ((InetSocketAddress) sock.getRemoteSocketAddress()).getPort());
		this.server = server;
		this.sock = sock;
	}
	
	public ServerConnection connect() throws ConnectException {
		Util.logger().info("Connecting to \"" + this.host + ":" + this.port + "\"...");
		
		try {
			this.input = new DataInputStream(this.sock.getInputStream());
			this.output = new DataOutputStream(this.sock.getOutputStream());
			this.connected = true;
			new ListenThread().start();
			new WriteThread().start();
		} catch(UnknownHostException e) {
			throw new ConnectException("Unknown host: " + this.host);
		} catch(IOException e) {
			throw new ConnectException("Failed to open stream: " + this.host, e);
		}
		
		return this;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getLoginKey() {
		return this.loginKey;
	}
	
	public void setLoginKey(String key) {
		this.loginKey = key;
	}
	
	public byte[] getToken() {
		return this.token;
	}
	
	public void setToken(byte token[]) {
		this.token = token;
	}
	
	public Server getServer() {
		return this.server;
	}

	public void send(Packet packet) {
		this.packets.add(packet);
	}
	
	public void disconnect(String reason) {
		this.disconnect(reason, true);
	}
	
	public void disconnect(String reason, boolean packet) {
		if(packet) this.send(new PacketDisconnect(reason));
		this.packets.clear();
		this.connected = false;
	}
	
	public void setAES() {
		BufferedBlockCipher in = new BufferedBlockCipher(new CFBBlockCipher(new AESFastEngine(), 8));
		in.init(false, new ParametersWithIV(new KeyParameter(this.key.getEncoded()), this.key.getEncoded(), 0, 16));
		BufferedBlockCipher out = new BufferedBlockCipher(new CFBBlockCipher(new AESFastEngine(), 8));
		out.init(true, new ParametersWithIV(new KeyParameter(this.key.getEncoded()), this.key.getEncoded(), 0, 16));
		this.input = new DataInputStream(new CipherInputStream(this.input, in));
		this.output = new DataOutputStream(new CipherOutputStream(this.output, out));
	}
	
	public SecretKey getSecretKey() {
		return this.key;
	}
	
	public void setSecretKey(SecretKey key) {
		this.key = key;
	}
	
	public boolean isConnected() {
		return this.connected;
	}
	
	private class ListenThread extends Thread {
		@Override
		public void run() {
			while(connected) {
				try {
					int opcode = input.readUnsignedByte();
					if(opcode < 0) {
						continue;
					}
					
					if(Packet.get(opcode) == null) {
						Util.logger().severe("Bad packet ID: " + opcode);
						disconnect("Bad packet ID: " + opcode);
						return;
					}
					
					Packet packet = Packet.get(opcode).newInstance();
					packet.read(input);
					packet.handleServer(ServerConnection.this);
					call(new PacketRecieveEvent(packet));
				} catch(Exception e) {
					Util.logger().severe("Error while listening to connection!");
					e.printStackTrace();
					disconnect("Error while listening to connection!");
				}
			}
		}
	}
	
	private class WriteThread extends Thread {
		@Override
		public void run() {
			while(connected) {
				if(packets.size() > 0) {
					Packet packet = packets.poll();
					
					try {
						output.write(packet.getId());
						packet.write(output);
						output.flush();
					} catch(Exception e) {
						Util.logger().severe("Error while writing packet \"" + packet.getId() + "\"!");
						e.printStackTrace();
						disconnect("Error while writing packet.");
					}
				}
			}
		}
	}
	
}
