package org.geysermc.mcprotocollib.auth.exception.profile;

import java.io.Serial;

/**
 * Thrown when a profile cannot be found.
 */
public class ProfileNotFoundException extends ProfileException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ProfileNotFoundException() {
    }

    public ProfileNotFoundException(String message) {
        super(message);
    }

    public ProfileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProfileNotFoundException(Throwable cause) {
        super(cause);
    }
}
