package org.geysermc.mcprotocollib.auth.exception.profile;

import java.io.Serial;

/**
 * Thrown when a profile-related error occurs.
 */
public class ProfileException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public ProfileException() {
    }

    public ProfileException(String message) {
        super(message);
    }

    public ProfileException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProfileException(Throwable cause) {
        super(cause);
    }
}
