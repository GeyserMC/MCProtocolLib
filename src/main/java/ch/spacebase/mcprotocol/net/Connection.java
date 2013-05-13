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
import ch.spacebase.mcprotocol.event.ProtocolEvent;
import ch.spacebase.mcprotocol.event.ProtocolListener;
import ch.spacebase.mcprotocol.exception.ConnectException;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.util.Util;

public abstract class Connection {

	protected Protocol protocol;
	protected String host;
	protected int port;

	private DataInputStream input;
	private DataOutputStream output;

	private Queue<Packet> packets = new ConcurrentLinkedQueue<Packet>();
	private boolean connected;

	private List<ProtocolListener> listeners = new ArrayList<ProtocolListener>();

	public Connection(Protocol prot, String host, int port) {
		this.protocol = prot;
		this.host = host;
		this.port = port;
	}

	public Protocol getProtocol() {
		return this.protocol;
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}

	public void listen(ProtocolListener listener) {
		this.listeners.add(listener);
	}

	public <T extends ProtocolEvent<ProtocolListener>> T call(T event) {
		for(ProtocolListener listener : this.listeners) {
			event.call(listener);
		}

		return event;
	}

	public abstract Connection connect() throws ConnectException;

	protected void connect(Socket sock) throws ConnectException {
		try {
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

	public DataInputStream getIn() {
		return this.input;
	}

	public DataOutputStream getOut() {
		return this.output;
	}

	public void setIn(InputStream in) {
		this.input = new DataInputStream(in);
	}

	public void setOut(OutputStream out) {
		this.output = new DataOutputStream(out);
	}

	public void send(Packet packet) {
		this.packets.add(packet);
	}

	public void disconnect(String reason) {
		this.disconnect(reason, true);
	}

	public void disconnect(String reason, boolean packet) {
		this.getProtocol().disconnected(this, reason, packet);
		this.packets.clear();
		this.connected = false;
		this.call(new DisconnectEvent(this, reason));
	}

	public boolean isConnected() {
		return this.connected;
	}

	private class ListenThread extends Thread {
		@Override
		public void run() {
			while(isConnected()) {
				try {
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
				} catch(EOFException e) {
					disconnect("End of Stream");
				} catch (Exception e) {
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
			while(isConnected()) {
				if(packets.size() > 0) {
					Packet packet = packets.poll();

					try {
						output.write(packet.getId());
						packet.write(output);
						output.flush();
					} catch (Exception e) {
						Util.logger().severe("Error while writing packet \"" + packet.getId() + "\"!");
						e.printStackTrace();
						disconnect("Error while writing packet.");
					}
				}
			}
		}
	}

}
