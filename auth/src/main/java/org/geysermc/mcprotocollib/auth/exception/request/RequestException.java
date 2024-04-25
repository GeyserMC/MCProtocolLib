package org.geysermc.mcprotocollib.auth.exception.request;

import java.io.Serial;

/**
 * Thrown when an error occurs while making an HTTP request.
 */
public class RequestException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
