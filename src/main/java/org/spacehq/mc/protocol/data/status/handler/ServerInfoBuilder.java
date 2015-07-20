package org.spacehq.mc.protocol.data.status.handler;

import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.packetlib.Session;

public interface ServerInfoBuilder {

    public ServerStatusInfo buildInfo(Session session);

}
