package org.geysermc.mcprotocollib.network.event.session;

import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.network.Session;
import org.jspecify.annotations.Nullable;

/**
 * Called when the session is disconnected.
 */
public class DisconnectedEvent implements SessionEvent {
    private final Session session;
    private final Component reason;
    private final @Nullable Throwable cause;

    /**
     * Creates a new DisconnectedEvent instance.
     *
     * @param session Session being disconnected.
     * @param reason Reason for the session to disconnect.
     * @param cause Throwable that caused the disconnect.
     */
    public DisconnectedEvent(Session session, Component reason, @Nullable Throwable cause) {
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
    public @Nullable Throwable getCause() {
        return this.cause;
    }

    @Override
    public void call(SessionListener listener) {
        listener.disconnected(this);
    }
}
