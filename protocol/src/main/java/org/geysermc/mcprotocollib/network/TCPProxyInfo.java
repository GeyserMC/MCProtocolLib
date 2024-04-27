package org.geysermc.mcprotocollib.network;

import java.net.SocketAddress;

/**
 * Information describing a network proxy.
 */
public class TCPProxyInfo {
    private final Type type;
    private final SocketAddress address;
    private String username;
    private String password;

    /**
     * Creates a new unauthenticated ProxyInfo instance.
     *
     * @param type    Type of proxy.
     * @param address Network address of the proxy.
     */
    public TCPProxyInfo(Type type, SocketAddress address) {
        this.type = type;
        this.address = address;
    }

    /**
     * Creates a new authenticated ProxyInfo instance.
     *
     * @param type    Type of proxy.
     * @param address Network address of the proxy.
     * @param username Username to authenticate with.
     * @param password Password to authenticate with.
     */
    public TCPProxyInfo(Type type, SocketAddress address, String username, String password) {
        this(type, address);
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
