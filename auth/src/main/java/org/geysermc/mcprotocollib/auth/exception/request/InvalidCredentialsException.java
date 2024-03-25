package org.geysermc.mcprotocollib.auth.exception.request;

import java.io.Serial;

/**
 * Thrown when invalid credentials are provided.
 */
public class InvalidCredentialsException extends RequestException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidCredentialsException() {
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCredentialsException(Throwable cause) {
        super(cause);
    }
}
