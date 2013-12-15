package ch.spacebase.mc.protocol.data.status.handler;

import ch.spacebase.mc.protocol.data.status.ServerStatusInfo;


public interface ServerInfoHandler {

	public void handle(ServerStatusInfo info);
	
}
