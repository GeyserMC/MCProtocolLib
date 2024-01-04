package org.geysermc.mc.protocol.data.status.handler;

import org.geysermc.packetlib.Session;

public interface ServerPingTimeHandler {
    void handle(Session session, long pingTime);
}
