package com.github.steveice10.mc.protocol;

import com.github.steveice10.mc.protocol.data.ProtocolState;
import com.github.steveice10.packetlib.Session;

/**
 * Interface for handling a session logging in to a server.
 */
public interface ServerLoginHandler {
    /**
     * Called when a session completes the initial login process and is now in the {@link ProtocolState}.GAME.
     *
     * @param session Session that logged in.
     */
    public void loggedIn(Session session);
}
