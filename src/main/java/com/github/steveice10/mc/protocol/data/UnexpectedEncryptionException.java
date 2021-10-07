package com.github.steveice10.mc.protocol.data;

/**
 * Thrown whenever a ClientboundHelloPacket is sent when we aren't expecting it
 * (I.E.: online mode server and offline mode client)
 */
public class UnexpectedEncryptionException extends IllegalStateException {
    public UnexpectedEncryptionException() {
        super("Cannot reply to ClientboundHelloPacket without profile and access token.");
    }
}
