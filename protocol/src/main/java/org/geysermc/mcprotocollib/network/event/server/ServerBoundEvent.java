package org.geysermc.mcprotocollib.network.event.server;

import org.geysermc.mcprotocollib.network.Server;

/**
 * Called when the server is bound to its host and port.
 */
public class ServerBoundEvent implements ServerEvent {
    private final Server server;

    /**
     * Creates a new ServerBoundEvent instance.
     *
     * @param server Server being bound.
     */
    public ServerBoundEvent(Server server) {
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
        listener.serverBound(this);
    }
}
