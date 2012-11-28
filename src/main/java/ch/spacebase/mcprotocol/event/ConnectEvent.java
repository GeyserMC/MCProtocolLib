package ch.spacebase.mcprotocol.event;

import ch.spacebase.mcprotocol.net.ServerConnection;

public class ConnectEvent extends ProtocolEvent<ServerListener> {

	private ServerConnection conn;

	public ConnectEvent(ServerConnection conn) {
		this.conn = conn;
	}

	public ServerConnection getConnection() {
		return this.conn;
	}

	@Override
	public void call(ServerListener listener) {
		listener.onConnect(this);
	}

}
