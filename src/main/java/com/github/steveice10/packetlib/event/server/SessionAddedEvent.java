package com.github.steveice10.packetlib.event.server;

import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;

/**
 * Called when a session is added to the server.
 *
 * @param server  The server the session is being added to.
 * @param session Session being added.
 */
public record SessionAddedEvent(Server server, Session session) implements ServerEvent {
    @Override
    public void call(ServerListener listener) {
        listener.sessionAdded(this);
    }
}
