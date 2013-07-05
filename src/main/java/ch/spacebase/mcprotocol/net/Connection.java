package ch.spacebase.mcprotocol.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import ch.spacebase.mcprotocol.event.DisconnectEvent;
import ch.spacebase.mcprotocol.event.PacketRecieveEvent;
import ch.spacebase.mcprotocol.event.PacketSendEvent;
import ch.spacebase.mcprotocol.event.ProtocolEvent;
import ch.spacebase.mcprotocol.event.ProtocolListener;
import ch.spacebase.mcprotocol.exception.ConnectException;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.util.Util;

/**
 * A network connection.
 */
public abstract class Connection {

	/**
	 * The connection's protocol.
	 */
	protected Protocol protocol;
	
	/**
	 * The connection's remote host.
	 */
	protected String host;
	
	/**
	 * The connection's remote port.
	 */
	protected int port;

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
	 * The connection's packet queue.
	 */
	private Queue<Packet> packets = new ConcurrentLinkedQueue<Packet>();
	
	/**
	 * The connection's status.
	 */
	private boolean connected;
	
	/**
	 * Whether the connection is currently reading.
	 */
	private boolean reading = false;
	
	/**
	 * Whether the connection is currently writing.
	 */
	private boolean writing = false;

	/**
	 * Listeners listening to this connection.
	 */
	private List<ProtocolListener> listeners = new ArrayList<ProtocolListener>();

	/**
	 * Creates a new connection.
	 * @param prot Protocol of the connection.
	 * @param host Remote host of the connection.
	 * @param port Remote port of the connection.
	 */
	public Connection(Protocol prot, String host, int port) {
		this.protocol = prot;
		this.host = host;
		this.port = port;
	}

	/**
	 * Gets the protocol of this connection.
	 * @return The connection's protocol.
	 */
	public Protocol getProtocol() {
		return this.protocol;
	}

	/**
	 * Gets the remote host of this connection.
	 * @return The connection's remote host.
	 */
	public String getRemoteHost() {
		return this.host;
	}

	/**
	 * Gets the remote port of this connection.
	 * @return The connection's remote port.
	 */
	public int getRemotePort() {
		return this.port;
	}
	
	/**
	 * Gets whether this connection is connected.
	 * @return Whether the connection is connected.
	 */
	public boolean isConnected() {
		return this.connected;
	}

	/**
	 * Adds a listener to listen to this connection.
	 * @param listener Listener to add.
	 */
	public void listen(ProtocolListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Calls an event for this connection.
	 * @param event Event to call.
	 * @return The event called.
	 */
	public <T extends ProtocolEvent<ProtocolListener>> T call(T event) {
		for(ProtocolListener listener : this.listeners) {
			event.call(listener);
		}

		return event;
	}

	/**
	 * Connects the connection to its remote host.
	 * @return This connection.
	 * @throws ConnectException If a connection error occurs.
	 */
	public abstract Connection connect() throws ConnectException;

	/**
	 * Connects this connection using the given socket.
	 * @param sock Socket to use.
	 * @throws ConnectException If a connection error occurs.
	 */
	protected void connect(Socket sock) throws ConnectException {
		try {
			this.sock = sock;
			this.input = new DataInputStream(sock.getInputStream());
			this.output = new DataOutputStream(sock.getOutputStream());
			this.connected = true;
			new ListenThread().start();
			new WriteThread().start();
		} catch (UnknownHostException e) {
			throw new ConnectException("Unknown host: " + this.host);
		} catch (IOException e) {
			throw new ConnectException("Failed to open stream: " + this.host, e);
		}
	}

	/**
	 * Gets the input stream of this connection.
	 * @return The connection's input stream.
	 */
	public DataInputStream getIn() {
		return this.input;
	}

	/**
	 * Gets the output stream of this connection.
	 * @return The connection's output stream.
	 */
	public DataOutputStream getOut() {
		return this.output;
	}

	/**
	 * Sets the input stream of this connection.
	 * @param in The new input stream.
	 */
	public void setIn(InputStream in) {
		this.input = new DataInputStream(in);
	}

	/**
	 * Sets the output stream of this connection.
	 * @param out The new output stream.
	 */
	public void setOut(OutputStream out) {
		this.output = new DataOutputStream(out);
	}

	/**
	 * Sends a packet.
	 * @param packet Packet to send.
	 */
	public void send(Packet packet) {
		this.packets.add(packet);
	}

	/**
	 * Disconnects this connection.
	 * @param reason Reason for disconnecting.
	 */
	public void disconnect(String reason) {
		this.disconnect(reason, true);
	}

	/**
	 * Disconnects this connection.
	 * @param reason Reason for disconnecting.
	 * @param packet Whether to send a packet on disconnect.
	 */
	public void disconnect(String reason, boolean packet) {
		this.getProtocol().disconnected(this, reason, packet);
		this.packets.clear();
		this.connected = false;
		this.call(new DisconnectEvent(this, reason));
		new CloseThread().start();
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

					if(protocol.getType().getPacket(opcode) == null) {
						Util.logger().severe("Bad packet ID: " + opcode);
						disconnect("Bad packet ID: " + opcode);
						return;
					}

					Packet packet = protocol.getType().getPacket(opcode).newInstance();
					packet.read(input);
					if(Connection.this instanceof Client) {
						packet.handleClient((Client) Connection.this);
					} else {
						packet.handleServer((ServerConnection) Connection.this);
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
