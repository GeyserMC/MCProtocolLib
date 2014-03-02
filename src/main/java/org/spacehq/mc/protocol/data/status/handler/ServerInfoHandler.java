package org.spacehq.mc.protocol.data.status.handler;

import org.spacehq.mc.protocol.data.status.ServerStatusInfo;


public interface ServerInfoHandler {

	public void handle(ServerStatusInfo info);

}
