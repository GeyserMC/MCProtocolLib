package org.geysermc.mcprotocollib.auth.exception.request;

import java.io.Serial;

public class XboxRequestException extends RequestException {
    @Serial
    private static final long serialVersionUID = 1L;

    public XboxRequestException(String message) {
        super(message);
    }
}
