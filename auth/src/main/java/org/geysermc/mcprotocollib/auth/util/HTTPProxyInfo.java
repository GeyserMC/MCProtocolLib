package org.geysermc.mcprotocollib.auth.util;

/**
 * Information describing an HTTP network proxy.
 */
public record HTTPProxyInfo(Type type, String host, int port, String username, String password) {
    /**
     * Creates a new unauthenticated ProxyInfo instance.
     *
     * @param type Type of proxy.
     */
    public HTTPProxyInfo(Type type, String host, int port) {
        this(type, host, port, null, null);
    }

    /**
     * Supported proxy types.
     */
    public enum Type {
        /**
         * HTTP proxy.
         */
        HTTP,

        /**
         * SOCKS4 proxy.
         */
        SOCKS4,

        /**
         * SOCKS5 proxy.
         */
        SOCKS5;
    }
}
