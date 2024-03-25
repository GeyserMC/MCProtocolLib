package org.geysermc.mcprotocollib.auth.exception.profile;

import java.io.Serial;

/**
 * Thrown when an error occurs while looking up a profile.
 */
public class ProfileLookupException extends ProfileException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ProfileLookupException() {
    }

    public ProfileLookupException(String message) {
        super(message);
    }

    public ProfileLookupException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProfileLookupException(Throwable cause) {
        super(cause);
    }
}
