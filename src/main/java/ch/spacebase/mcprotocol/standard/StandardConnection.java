package ch.spacebase.mcprotocol.standard;

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

import ch.spacebase.mcprotocol.event.DisconnectEvent;
import ch.spacebase.mcprotocol.event.PacketRecieveEvent;
import ch.spacebase.mcprotocol.event.PacketSendEvent;
import ch.spacebase.mcprotocol.exception.ConnectException;
import ch.spacebase.mcprotocol.net.BaseConnection;
import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.Connection;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.io.StandardInput;
import ch.spacebase.mcprotocol.standard.io.StandardOutput;
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
	private StandardInput input;
	
	/**
	 * The connection's output stream.
	 */
	private StandardOutput output;
	
	/**
	 * The connection's packet write queue.
	 */
	private Queue<Packet> packets = new ConcurrentLinkedQueue<Packet>();
	
	/**
	 * Whether the connection is writing.
	 */
	private boolean writing = false;
	
	/**
	 * Whether the connection is connected.
	 */
	private boolean connected = false;
	
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
		super(new StandardPackets(), host, port);
	}

	@Override
	public boolean isConnected() {
		return this.connected;
	}

	/**
	 * Connects using the given socket.
	 * @param sock Socket to use.
	 * @throws ConnectException If a connection error occurs.
	 */
	protected void connect(Socket sock) throws ConnectException {
		this.sock = sock;
		try {
			this.input = new StandardInput(this.sock.getInputStream());
			this.output = new StandardOutput(this.sock.getOutputStream());
			this.connected = true;
			new ListenThread().start();
			new WriteThread().start();
		} catch (UnknownHostException e) {
			throw new ConnectException("Unknown host: " + this.getRemoteHost());
		} catch (IOException e) {
			throw new ConnectException("Failed to open stream: " + this.getRemoteHost(), e);
		}
	}
	
	@Override
	public void disconnect(String reason) {
		this.disconnect(reason, true);
	}

	@Override
	public void disconnect(String reason, boolean packet) {
		this.packets.clear();
		if(packet && this.isConnected()) {
			this.send(new PacketDisconnect(reason));
		}
		
		new CloseThread().start();
		this.connected = false;
		this.call(new DisconnectEvent(this, reason));
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

		this.input = new StandardInput(new CipherInputStream(this.input.getStream(), in));
		this.output = new StandardOutput(new CipherOutputStream(this.output.getStream(), out));
	}
	
	/**
	 * A thread listening for incoming packets.
	 */
	private class ListenThread extends Thread {
		@Override
		public void run() {
			while(isConnected()) {
				try {
					int opcode = input.readUnsignedByte();
					if(opcode < 0) {
						continue;
					}

					if(getPacketRegistry().getPacket(opcode) == null) {
						Util.logger().severe("Bad packet ID: " + opcode);
						disconnect("Bad packet ID: " + opcode);
						return;
					}
					
					Packet packet = getPacketRegistry().getPacket(opcode).newInstance();
					packet.read(input);
					call(new PacketRecieveEvent(packet));
					if(StandardConnection.this instanceof Client) {
						packet.handleClient((Client) StandardConnection.this);
					} else if(StandardConnection.this instanceof ServerConnection) {
						packet.handleServer((ServerConnection) StandardConnection.this);
					}
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
						output.writeByte(packet.getId());
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
				
				writing = false;
			}
		}
	}
	
	/**
	 * A thread that waits for the connection to finish writing before closing it.
	 */
	private class CloseThread extends Thread {
		@Override
		public void run() {
			while(writing) {
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
			
			connected = false;
		}
	}

}
