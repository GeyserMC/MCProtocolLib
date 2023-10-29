package com.github.steveice10.packetlib.event.server;

import com.github.steveice10.packetlib.Server;

/**
 * Called when the server is closed.
 *
 * @param server The server being closed.
 */
public record ServerClosedEvent(Server server) implements ServerEvent {
    @Override
    public void call(ServerListener listener) {
        listener.serverClosed(this);
    }
}
