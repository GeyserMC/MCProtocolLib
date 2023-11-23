package com.github.steveice10.mc.auth.exception.request;

/**
 * Thrown when authorisation for a msa oauth code is still pending
 */
public class AuthPendingException extends RequestException {
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
