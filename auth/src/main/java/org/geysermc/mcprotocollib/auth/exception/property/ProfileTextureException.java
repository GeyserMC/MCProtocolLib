package org.geysermc.mcprotocollib.auth.exception.property;

import java.io.Serial;

/**
 * Thrown when an error occurs while retrieving a profile texture.
 */
public class ProfileTextureException extends PropertyException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ProfileTextureException() {
    }

    public ProfileTextureException(String message) {
        super(message);
    }

    public ProfileTextureException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProfileTextureException(Throwable cause) {
        super(cause);
    }
}
