package org.geysermc.mcprotocollib.auth.exception.profile;

import java.io.Serial;

/**
 * Thrown when a profile cannot be found.
 */
public class ProfileNotFoundException extends ProfileException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ProfileNotFoundException(String message) {
        super(message);
    }
}
