package org.geysermc.mcprotocollib.protocol;

import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.protocol.data.ProtocolState;

/**
 * Interface for handling a session logging in to a server.
 */
public interface ServerLoginHandler {
    /**
     * Called when a session completes the initial login process and is now in the {@link ProtocolState}.GAME.
     *
     * @param session Session that logged in.
     */
    void loggedIn(Session session);
}
