package com.github.steveice10.mc.protocol.data;

import java.io.Serial;

/**
 * Thrown whenever a ClientboundHelloPacket is sent when we aren't expecting it
 * (I.E.: online mode server and offline mode client)
 */
public class UnexpectedEncryptionException extends IllegalStateException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UnexpectedEncryptionException() {
        super("Cannot reply to ClientboundHelloPacket without profile and access token.");
    }
}
