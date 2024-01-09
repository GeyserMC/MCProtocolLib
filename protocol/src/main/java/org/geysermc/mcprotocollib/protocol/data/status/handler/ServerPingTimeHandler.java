package org.geysermc.mcprotocollib.protocol.data.status.handler;

import org.geysermc.mcprotocollib.network.Session;

public interface ServerPingTimeHandler {
    void handle(Session session, long pingTime);
}
