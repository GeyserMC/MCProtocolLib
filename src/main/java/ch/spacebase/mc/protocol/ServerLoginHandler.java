package ch.spacebase.mc.protocol;

import ch.spacebase.packetlib.Session;

public interface ServerLoginHandler {
	
	public void loggedIn(Session session);
	
}
