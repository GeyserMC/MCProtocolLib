package ch.spacebase.packetlib.test;

import ch.spacebase.packetlib.event.server.ServerAdapter;
import ch.spacebase.packetlib.event.server.ServerClosedEvent;
import ch.spacebase.packetlib.event.server.ServerClosingEvent;
import ch.spacebase.packetlib.event.server.SessionAddedEvent;
import ch.spacebase.packetlib.event.server.SessionRemovedEvent;

public class ServerListener extends ServerAdapter {

	@Override
	public void serverClosing(ServerClosingEvent event) {
		System.out.println("CLOSING SERVER...");
	}

	@Override
	public void serverClosed(ServerClosedEvent event) {
		System.out.println("SERVER CLOSED");
	}

	@Override
	public void sessionAdded(SessionAddedEvent event) {
		System.out.println("SESSION ADDED: " + event.getSession().getHost() + ":" + event.getSession().getPort());
	}

	@Override
	public void sessionRemoved(SessionRemovedEvent event) {
		System.out.println("SESSION REMOVED: " + event.getSession().getHost() + ":" + event.getSession().getPort());
		event.getServer().close();
	}

}
