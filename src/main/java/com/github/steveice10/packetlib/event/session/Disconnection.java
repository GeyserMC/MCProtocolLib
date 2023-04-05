package com.github.steveice10.packetlib.event.session;

import com.github.steveice10.packetlib.Session;
import net.kyori.adventure.text.Component;

public class Disconnection implements SessionEvent {
    protected final Session session;
    protected final Component reason;
    protected final Throwable cause;

    public Disconnection(Session session,Component reason, Throwable cause) {
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
     * Gets the reason given for the session disconnection.
     *
     * @return The event's reason.
     */
    public Component getReason() {
        return this.reason;
    }

    /**
     * Gets the Throwable responsible for the session disconnection.
     *
     * @return The Throwable responsible for the disconnect, or null if the disconnect was not caused by a Throwable.
     */
    public Throwable getCause() {
        return this.cause;
    }

    @Override
    public void call(SessionListener listener) {
    }
}
