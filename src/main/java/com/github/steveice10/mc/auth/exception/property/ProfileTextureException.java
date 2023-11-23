package com.github.steveice10.mc.auth.exception.property;

/**
 * Thrown when an error occurs while retrieving a profile texture.
 */
public class ProfileTextureException extends PropertyException {
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
