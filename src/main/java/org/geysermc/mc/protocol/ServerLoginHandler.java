package org.geysermc.mc.protocol;

import org.geysermc.mc.protocol.data.ProtocolState;
import org.geysermc.packetlib.Session;

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
