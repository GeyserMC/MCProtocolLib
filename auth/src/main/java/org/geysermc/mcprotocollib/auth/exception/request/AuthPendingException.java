package org.geysermc.mcprotocollib.auth.exception.request;

import java.io.Serial;

/**
 * Thrown when authorisation for a msa oauth code is still pending
 */
public class AuthPendingException extends RequestException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AuthPendingException() {
    }

    public AuthPendingException(String message) {
        super(message);
    }

    public AuthPendingException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthPendingException(Throwable cause) {
        super(cause);
    }
}
