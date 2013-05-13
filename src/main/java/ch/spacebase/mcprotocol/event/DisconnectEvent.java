package ch.spacebase.mcprotocol.event;

import ch.spacebase.mcprotocol.net.Connection;

public class DisconnectEvent extends ProtocolEvent<ProtocolListener> {

	private Connection conn;

	public DisconnectEvent(Connection conn) {
		this.conn = conn;
	}

	public Connection getConnection() {
		return this.conn;
	}

	@Override
	public void call(ProtocolListener listener) {
		listener.onDisconnect(this);
	}

}
