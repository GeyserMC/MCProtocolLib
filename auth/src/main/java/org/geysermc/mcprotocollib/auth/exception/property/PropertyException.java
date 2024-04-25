package org.geysermc.mcprotocollib.auth.exception.property;

import java.io.Serial;

/**
 * Thrown when a property-related error occurs.
 */
public class PropertyException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public PropertyException(String message) {
        super(message);
    }

    public PropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}
