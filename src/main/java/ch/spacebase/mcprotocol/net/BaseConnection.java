package ch.spacebase.mcprotocol.net;

import java.util.ArrayList;
import java.util.List;

import ch.spacebase.mcprotocol.event.ProtocolEvent;
import ch.spacebase.mcprotocol.event.ProtocolListener;

/**
 * A basic connection class.
 */
public abstract class BaseConnection implements Connection {

	/**
	 * The connection's remote host.
	 */
	private String host;
	
	/**
	 * The connection's remote port.
	 */
	private int port;
	
	/**
	 * The connection's username.
	 */
	private String username;
	
	/**
	 * The connection's packets.
	 */
	private PacketRegistry packets;

	/**
	 * Listeners listening to this connection.
	 */
	private List<ProtocolListener> listeners = new ArrayList<ProtocolListener>();
	
	/**
	 * Creates a new connection.
	 * @param host Host to connect to.
	 * @param port Port to connect to.
	 */
	public BaseConnection(PacketRegistry packets, String host, int port) {
		this.packets = packets;
		this.host = host;
		this.port = port;
	}

	@Override
	public String getRemoteHost() {
		return this.host;
	}

	@Override
	public int getRemotePort() {
		return this.port;
	}
	
	@Override
	public PacketRegistry getPacketRegistry() {
		return this.packets;
	}
	
	@Override
	public String getUsername() {
		return this.username;
	}
	
	@Override
	public void setUsername(String name) {
		if(this.username != null) {
			return;
		}
		
		this.username = name;
	}
	
	@Override
	public void listen(ProtocolListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public <T extends ProtocolEvent<ProtocolListener>> T call(T event) {
		for(ProtocolListener listener : this.listeners) {
			event.call(listener);
		}

		return event;
	}
	
}
