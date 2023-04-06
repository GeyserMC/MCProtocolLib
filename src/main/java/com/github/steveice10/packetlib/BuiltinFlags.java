package com.github.steveice10.packetlib;

/**
 * Built-in PacketLib session flags.
 */
public class BuiltinFlags {
    /**
     * When set to true, enables printing internal debug messages.
     */
    public static final String PRINT_DEBUG = "print-packetlib-debug";

    public static final String ENABLE_CLIENT_PROXY_PROTOCOL = "enable-client-proxy-protocol";

    public static final String CLIENT_PROXIED_ADDRESS = "client-proxied-address";

    /**
     * When set to false, an SRV record resolve is not attempted.
     */
    public static final String ATTEMPT_SRV_RESOLVE = "attempt-srv-resolve";

    private BuiltinFlags() {
    }
}
