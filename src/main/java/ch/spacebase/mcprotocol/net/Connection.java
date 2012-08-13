package ch.spacebase.mcprotocol.net;

import java.util.ArrayList;
import java.util.List;

import ch.spacebase.mcprotocol.event.ProtocolEvent;
import ch.spacebase.mcprotocol.event.ProtocolListener;
import ch.spacebase.mcprotocol.exception.ConnectException;
import ch.spacebase.mcprotocol.net.packet.Packet;

public abstract class Connection {

	protected String host;
	protected int port;
	
	private List<ProtocolListener> listeners = new ArrayList<ProtocolListener>();
	
	public Connection(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void listen(ProtocolListener listener) {
		this.listeners.add(listener);
	}
	
	public <T extends ProtocolEvent> T call(T event) {
		for(ProtocolListener listener : this.listeners) {
			event.call(listener);
		}
		
		return event;
	}
	
	public abstract Connection connect() throws ConnectException;
	
	public abstract void disconnect(String reason);
	
	public abstract void send(Packet packet);
	
}
