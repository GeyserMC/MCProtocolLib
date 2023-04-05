package com.github.steveice10.packetlib.event.session;

import com.github.steveice10.packetlib.Session;
import net.kyori.adventure.text.Component;

/**
 * Called when the session is about to disconnect.
 */
public class DisconnectingEvent extends Disconnection {

    /**
     * Creates a new DisconnectingEvent instance.
     *
     * @param session Session being disconnected.
     * @param reason  Reason for the session to disconnect.
     * @param cause   Throwable that caused the disconnect.
     */
    public DisconnectingEvent(Session session, Component reason, Throwable cause) {
        super(session,reason,cause);
    }


    @Override
    public void call(SessionListener listener) {
        listener.disconnecting(this);
    }
}
