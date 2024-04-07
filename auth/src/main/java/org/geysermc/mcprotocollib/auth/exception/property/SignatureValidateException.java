package org.geysermc.mcprotocollib.auth.exception.property;

import java.io.Serial;

/**
 * Thrown when an error occurs while validating a signature.
 */
public class SignatureValidateException extends PropertyException {
    @Serial
    private static final long serialVersionUID = 1L;

    public SignatureValidateException(String message, Throwable cause) {
        super(message, cause);
    }
}
