package ch.spacebase.mcprotocol.standard;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
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
import ch.spacebase.mcprotocol.event.PacketSendEvent;
import ch.spacebase.mcprotocol.exception.ConnectException;
import ch.spacebase.mcprotocol.net.BaseConnection;
import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.Connection;
import ch.spacebase.mcprotocol.net.Protocol;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.packet.PacketDisconnect;
import ch.spacebase.mcprotocol.util.Util;

/**
 * A connection implementing standard Minecraft protocol.
 */
public abstract class StandardConnection extends BaseConnection {
	
	/**
	 * The connection's socket.
	 */
	private Socket sock;
	
	/**
	 * The connection's input stream.
	 */
	private DataInputStream input;
	
	/**
	 * The connection's output stream.
	 */
	private DataOutputStream output;
	
	/**
	 * The connection's packet write queue.
	 */
	private Queue<Packet> packets = new ConcurrentLinkedQueue<Packet>();
	
	/**
	 * Whether the connection is reading.
	 */
	private boolean reading = false;
	
	/**
	 * Whether the connection is writing.
	 */
	private boolean writing = false;
	
	/**
	 * The connection's secret key.
	 */
	private SecretKey key;
	
	/**
	 * Creates a new standard connection.
	 * @param host Host to connect to.
	 * @param port Port to connect to.
	 */
	public StandardConnection(String host, int port) {
		super(host, port);
	}
	
	@Override
	public Protocol getType() {
		return Protocol.STANDARD;
	}

	@Override
	public boolean isConnected() {
		return this.sock != null && !this.sock.isClosed();
	}

	/**
	 * Connects using the given socket.
	 * @param sock Socket to use.
	 * @throws ConnectException If a connection error occurs.
	 */
	protected void connect(Socket sock) throws ConnectException {
		this.sock = sock;
		try {
			this.input = new DataInputStream(this.sock.getInputStream());
			this.output = new DataOutputStream(this.sock.getOutputStream());
			new ListenThread().start();
			new WriteThread().start();
		} catch (UnknownHostException e) {
			throw new ConnectException("Unknown host: " + this.getRemoteHost());
		} catch (IOException e) {
			throw new ConnectException("Failed to open stream: " + this.getRemoteHost(), e);
		}
	}

	@Override
	public void disconnect() {
		new CloseThread().start();
	}

	@Override
	public void disconnect(String reason) {
		this.send(new PacketDisconnect(reason));
		this.disconnect();
	}

	@Override
	public void send(Packet packet) {
		this.packets.add(packet);
	}
	
	/**
	 * Gets the protocol's secret key.
	 * @return The protocol's secret key.
	 */
	public SecretKey getSecretKey() {
		return this.key;
	}

	/**
	 * Sets the protocol's secret key.
	 * @param key The new secret key.
	 */
	public void setSecretKey(SecretKey key) {
		this.key = key;
	}
	
	/**
	 * Enabled AES encryption on the connection.
	 * @param conn Connection to enable AES on.
	 */
	public void setAES(Connection conn) {
		BufferedBlockCipher in = new BufferedBlockCipher(new CFBBlockCipher(new AESFastEngine(), 8));
		in.init(false, new ParametersWithIV(new KeyParameter(this.key.getEncoded()), this.key.getEncoded(), 0, 16));
		BufferedBlockCipher out = new BufferedBlockCipher(new CFBBlockCipher(new AESFastEngine(), 8));
		out.init(true, new ParametersWithIV(new KeyParameter(this.key.getEncoded()), this.key.getEncoded(), 0, 16));

		this.input = new DataInputStream(new CipherInputStream(this.input, in));
		this.output = new DataOutputStream(new CipherOutputStream(this.output, out));
	}
	
	/**
	 * A thread listening for incoming packets.
	 */
	private class ListenThread extends Thread {
		@Override
		public void run() {
			while(isConnected()) {
				try {
					reading = true;
					int opcode = input.readUnsignedByte();
					if(opcode < 0) {
						continue;
					}

					if(getType().getPacket(opcode) == null) {
						Util.logger().severe("Bad packet ID: " + opcode);
						disconnect("Bad packet ID: " + opcode);
						return;
					}

					Packet packet = getType().getPacket(opcode).newInstance();
					packet.read(input);
					if(StandardConnection.this instanceof Client) {
						packet.handleClient((Client) StandardConnection.this);
					} else if(StandardConnection.this instanceof ServerConnection) {
						packet.handleServer((ServerConnection) StandardConnection.this);
					}
					
					call(new PacketRecieveEvent(packet));
					reading = false;
				} catch(EOFException e) {
					disconnect("End of Stream");
				} catch (Exception e) {
					Util.logger().severe("Error while listening to connection!");
					e.printStackTrace();
					disconnect("Error while listening to connection!");
				}
				
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	/**
	 * A thread writing outgoing packets.
	 */
	private class WriteThread extends Thread {
		@Override
		public void run() {
			while(isConnected()) {
				if(packets.size() > 0) {
					writing = true;
					Packet packet = packets.poll();
					call(new PacketSendEvent(packet));
					
					try {
						output.write(packet.getId());
						packet.write(output);
						output.flush();
					} catch (Exception e) {
						Util.logger().severe("Error while writing packet \"" + packet.getId() + "\"!");
						e.printStackTrace();
						disconnect("Error while writing packet.");
					}
					
					writing = false;
				}
				
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	/**
	 * A thread that waits for the connection to finish before closing it.
	 */
	private class CloseThread extends Thread {
		@Override
		public void run() {
			while(reading || writing) {
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
				}
			}
			
			try {
				sock.close();
			} catch (IOException e) {
				System.err.println("Failed to close socket.");
				e.printStackTrace();
			}
		}
	}

}