package com.github.steveice10.packetlib;

import java.net.SocketAddress;

/**
 * Information describing a network proxy.
 */
public class ProxyInfo {
    private Type type;
    private SocketAddress address;
    private boolean authenticated;
    private String username;
    private String password;

    /**
     * Creates a new unauthenticated ProxyInfo instance.
     *
     * @param type    Type of proxy.
     * @param address Network address of the proxy.
     */
    public ProxyInfo(Type type, SocketAddress address) {
        this.type = type;
        this.address = address;
        this.authenticated = false;
    }

    /**
     * Creates a new authenticated ProxyInfo instance.
     *
     * @param type    Type of proxy.
     * @param address Network address of the proxy.
     * @param username Username to authenticate with.
     * @param password Password to authenticate with.
     */
    public ProxyInfo(Type type, SocketAddress address, String username, String password) {
        this(type, address);
        this.authenticated = true;
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the proxy's type.
     *
     * @return The proxy's type.
     */
    public Type getType() {
        return this.type;
    }

    /**
     * Gets the proxy's network address.
     *
     * @return The proxy's network address.
     */
    public SocketAddress getAddress() {
        return this.address;
    }

    /**
     * Gets whether the proxy is authenticated with.
     *
     * @return Whether to authenticate with the proxy.
     */
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    /**
     * Gets the proxy's authentication username.
     *
     * @return The username to authenticate with.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the proxy's authentication password.
     *
     * @return The password to authenticate with.
     */
    public String getPassword() {
        return this.password;
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
