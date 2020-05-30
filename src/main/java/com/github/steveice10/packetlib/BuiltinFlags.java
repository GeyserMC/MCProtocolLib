package com.github.steveice10.packetlib;

/**
 * Built-in PacketLib session flags.
 */
public class BuiltinFlags {
    /**
     * When set to true, prints exceptions that occur when attempting
     * to resolve DNS SRV records, rather than silently ignoring them.
     */
    public static final String PRINT_DNS_ERRORS = "print-dns-errors";

    private BuiltinFlags() {
    }
}
