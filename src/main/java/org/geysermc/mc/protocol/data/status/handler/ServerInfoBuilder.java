package org.geysermc.mc.protocol.data.status.handler;

import org.geysermc.mc.protocol.data.status.ServerStatusInfo;
import org.geysermc.packetlib.Session;

public interface ServerInfoBuilder {
    ServerStatusInfo buildInfo(Session session);
}
