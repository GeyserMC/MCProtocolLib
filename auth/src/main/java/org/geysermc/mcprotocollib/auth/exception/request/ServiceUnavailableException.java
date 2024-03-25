package org.geysermc.mcprotocollib.auth.exception.request;

import java.io.Serial;

/**
 * Thrown when a service is unavailable.
 */
public class ServiceUnavailableException extends RequestException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ServiceUnavailableException() {
    }

    public ServiceUnavailableException(String message) {
        super(message);
    }

    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceUnavailableException(Throwable cause) {
        super(cause);
    }
}
