package org.geysermc.mcprotocollib.protocol.data.status.handler;

import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.protocol.data.status.ServerStatusInfo;

public interface ServerInfoHandler {
    void handle(Session session, ServerStatusInfo info);
}
