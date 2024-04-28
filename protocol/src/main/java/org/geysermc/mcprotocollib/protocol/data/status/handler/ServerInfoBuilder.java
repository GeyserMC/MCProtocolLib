package org.geysermc.mcprotocollib.protocol.data.status.handler;

import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.protocol.data.status.ServerStatusInfo;

public interface ServerInfoBuilder {
    ServerStatusInfo buildInfo(Session session);
}
