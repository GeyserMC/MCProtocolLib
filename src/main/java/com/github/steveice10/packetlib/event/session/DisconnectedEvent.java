package com.github.steveice10.packetlib.event.session;

import com.github.steveice10.packetlib.Session;
import net.kyori.adventure.text.Component;

/**
 * Called when the session is disconnected.
 *
 * @param session Session being disconnected.
 * @param reason  Reason for the session to disconnect.
 * @param cause   Throwable that caused the disconnect.
 * @see SessionListener#disconnected(DisconnectedEvent)
 */
public record DisconnectedEvent(Session session, Component reason, Throwable cause) implements SessionEvent {
    @Override
    public void call(SessionListener listener) {
        listener.disconnected(this);
    }
}
