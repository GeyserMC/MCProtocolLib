package org.geysermc.mcprotocollib.network;

/**
 * A network client session.
 */
public interface ClientSession extends Session {
    /**
     * Connects this session to its host and port.
     */
    default void connect() {
        connect(true);
    }

    /**
     * Connects this session to its host and port.
     *
     * @param wait Whether to wait for the connection to be established before returning.
     */
    void connect(boolean wait);
}
