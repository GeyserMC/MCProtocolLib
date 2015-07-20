package org.spacehq.mc.protocol.data.status.handler;

import org.spacehq.packetlib.Session;

public interface ServerPingTimeHandler {

    public void handle(Session session, long pingTime);

}
