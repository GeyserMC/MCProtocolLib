package com.github.steveice10.packetlib.event.server;

import com.github.steveice10.packetlib.Server;

/**
 * Called when the server is bound to its host and port.
 *
 * @param server The server being bound.
 */
public record ServerBoundEvent(Server server) implements ServerEvent {
    @Override
    public void call(ServerListener listener) {
        listener.serverBound(this);
    }
}
