package org.geysermc.mcprotocollib.network;

import java.net.InetSocketAddress;

/**
 * Built-in PacketLib session flags.
 */
public class BuiltinFlags {
    public static final Flag<InetSocketAddress> CLIENT_PROXIED_ADDRESS = new Flag<>("client-proxied-address", InetSocketAddress.class);

    /**
     * When set to false, an SRV record resolve is not attempted.
     */
    public static final Flag<Boolean> ATTEMPT_SRV_RESOLVE = new Flag<>("attempt-srv-resolve", Boolean.class);

    /**
     * When set to true, the client or server will attempt to use TCP Fast Open if supported.
     */
    public static final Flag<Boolean> TCP_FAST_OPEN = new Flag<>("tcp-fast-open", Boolean.class);

    public static final Flag<Integer> CLIENT_CONNECT_TIMEOUT = new Flag<>("client-connect-timeout", Integer.class);
    public static final Flag<Integer> READ_TIMEOUT = new Flag<>("read-timeout", Integer.class);
    public static final Flag<Integer> WRITE_TIMEOUT = new Flag<>("write-timeout", Integer.class);

    private BuiltinFlags() {
    }
}
