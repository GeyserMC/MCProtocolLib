package com.github.steveice10.packetlib.event.server;

import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;

/**
 * Called when a session is removed and disconnected from the server.
 *
 * @param server  The server the session is being removed from.
 * @param session Session being removed.
 */
public record SessionRemovedEvent(Server server, Session session) implements ServerEvent {
    @Override
    public void call(ServerListener listener) {
        listener.sessionRemoved(this);
    }
}
