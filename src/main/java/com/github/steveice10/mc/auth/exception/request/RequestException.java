package com.github.steveice10.mc.auth.exception.request;

/**
 * Thrown when an error occurs while making an HTTP request.
 */
public class RequestException extends Exception {
    private static final long serialVersionUID = 1L;

    public RequestException() {
    }

    public RequestException(String message) {
        super(message);
    }

    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestException(Throwable cause) {
        super(cause);
    }
}
