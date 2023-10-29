package com.github.steveice10.packetlib;

import org.jetbrains.annotations.Nullable;

import java.net.SocketAddress;

/**
 * Information describing a network proxy.
 *
 * @param type     Type of proxy.
 * @param address  Network address of the proxy.
 * @param authData Authentication data for the proxy.
 */
public record ProxyInfo(Type type, SocketAddress address, ProxyAuthData authData) {
    /**
     * Creates a new unauthenticated ProxyInfo instance.
     *
     * @param type    Type of proxy.
     * @param address Network address of the proxy.
     */
    public ProxyInfo(Type type, SocketAddress address) {
        this(type, address, null);
    }

    /**
     * Creates a new authenticated ProxyInfo instance.
     *
     * @param type     Type of proxy.
     * @param address  Network address of the proxy.
     * @param username Username to authenticate with.
     * @param password Password to authenticate with.
     */
    public ProxyInfo(Type type, SocketAddress address, @Nullable String username, @Nullable String password) {
        this(type, address, new ProxyAuthData(username, password));
    }

    public boolean isAuthenticated() {
        return authData != null;
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
         * SOCKS4 does not support passwords.
         */
        SOCKS4,

        /**
         * SOCKS5 proxy.
         */
        SOCKS5
    }

    public record ProxyAuthData(String username, String password) {
    }
}
