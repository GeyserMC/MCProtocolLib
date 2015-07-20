package org.spacehq.packetlib.event.server;

import org.spacehq.packetlib.Server;

/**
 * Called when the server is closed.
 */
public class ServerClosedEvent implements ServerEvent {
    private Server server;

    /**
     * Creates a new ServerClosedEvent instance.
     *
     * @param server Server being closed.
     */
    public ServerClosedEvent(Server server) {
        this.server = server;
    }

    /**
     * Gets the server involved in this event.
     *
     * @return The event's server.
     */
    public Server getServer() {
        return this.server;
    }

    @Override
    public void call(ServerListener listener) {
        listener.serverClosed(this);
    }
}
