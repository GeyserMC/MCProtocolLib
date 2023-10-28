package com.github.steveice10.packetlib.event.session;

import com.github.steveice10.packetlib.Session;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Called when a session encounters an error while reading or writing packet data.
 */
@RequiredArgsConstructor
public class PacketErrorEvent implements SessionEvent {
    @Getter
    private final Session session;
    @Getter
    private final Throwable cause;
    private boolean suppress = false;

    /**
     * Gets whether the error should be suppressed. If the error is not suppressed,
     * it will be passed on through internal error handling and disconnect the session.
     * <br/>
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
