package ch.spacebase.mcprotocol.event;

import ch.spacebase.mcprotocol.net.Connection;

public class DisconnectEvent extends ProtocolEvent<ProtocolListener> {

	private Connection conn;
	private String reason;
	
	public DisconnectEvent(Connection conn, String reason) {
		this.conn = conn;
		this.reason = reason;
	}

	public Connection getConnection() {
		return this.conn;
	}
	
	public String getReason() {
		return this.reason;
	}
	
	@Override
	public void call(ProtocolListener listener) {
		listener.onDisconnect(this);
	}

}
