package org.geysermc.mcprotocollib.auth.util;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Information describing a network proxy.
 */
public record ProxyInfo(Type type, SocketAddress address, String username, String password) {
    /**
     * Creates a new unauthenticated proxy info.
     *
     * @param type Type of proxy.
     */
    public ProxyInfo(Type type, String host, int port, String username, String password) {
        this(type, new InetSocketAddress(host, port), username, password);
    }

    /**
     * Creates a new unauthenticated proxy info.
     *
     * @param type Type of proxy.
     */
    public ProxyInfo(Type type, SocketAddress address) {
        this(type, address, null, null);
    }

    /**
     * Creates a new unauthenticated proxy info.
     *
     * @param type Type of proxy.
     */
    public ProxyInfo(Type type, String host, int port) {
        this(type, new InetSocketAddress(host, port));
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
