package com.github.steveice10.packetlib.event.session;

import com.github.steveice10.packetlib.Session;

/**
 * Called when a session encounters an error while reading or writing packet data.
 */
public class PacketErrorEvent implements SessionEvent {
    private Session session;
    private Throwable cause;
    private boolean suppress = false;

    /**
     * Creates a new SessionErrorEvent instance.
     *
     * @param session Session that the error came from.
     * @param cause   Cause of the error.
     */
    public PacketErrorEvent(Session session, Throwable cause) {
        this.session = session;
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
     * Gets the Throwable responsible for the error.
     *
     * @return The Throwable responsible for the error.
     */
    public Throwable getCause() {
        return this.cause;
    }

    /**
     * Gets whether the error should be suppressed. If the error is not suppressed,
     * it will be passed on through internal error handling and disconnect the session.
     *
     * The default value is false.
     *
     * @return Whether the error should be suppressed.
     */
    public boolean shouldSuppress() {
        return this.suppress;
    }

    /**
     * Sets whether the error should be suppressed. If the error is not suppressed,
     * it will be passed on through internal error handling and disconnect the session.
     *
     * @param suppress Whether the error should be suppressed.
     */
    public void setSuppress(boolean suppress) {
        this.suppress = suppress;
    }

    @Override
    public void call(SessionListener listener) {
        listener.packetError(this);
    }
}
