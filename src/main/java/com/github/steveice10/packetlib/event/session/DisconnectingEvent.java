package com.github.steveice10.packetlib.event.session;

import com.github.steveice10.packetlib.Session;
import net.kyori.adventure.text.Component;

/**
 * Called when the session is about to disconnect.
 */
public class DisconnectingEvent implements SessionEvent {
    private final Session session;
    private final Component reason;
    private final Throwable cause;

    /**
     * Creates a new DisconnectingEvent instance.
     *
     * @param session Session being disconnected.
     * @param reason  Reason for the session to disconnect.
     * @param cause   Throwable that caused the disconnect.
     */
    public DisconnectingEvent(Session session, Component reason, Throwable cause) {
        this.session = session;
        this.reason = reason;
        this.cause = cause;
    }

    /**
     * Gets the session involved in this event.
     *
     * @return The event's session.
     */
    public Session getSession() {
        return this.session;
    }

    /**
     * Gets the reason given for the session disconnecting.
     *
     * @return The event's reason.
     */
    public Component getReason() {
        return this.reason;
    }

    /**
     * Gets the Throwable responsible for the session disconnecting.
     *
     * @return The Throwable responsible for the disconnect, or null if the disconnect was not caused by a Throwable.
     */
    public Throwable getCause() {
        return this.cause;
    }

    @Override
    public void call(SessionListener listener) {
        listener.disconnecting(this);
    }
}
