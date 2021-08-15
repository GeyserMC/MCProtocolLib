package com.github.steveice10.packetlib.helper;

import io.netty.channel.epoll.Epoll;
import io.netty.incubator.channel.uring.IOUring;

public class TransportHelper {
    public enum TransportMethod {
        NIO, EPOLL, IO_URING;
    }

    public static TransportMethod determineTransportMethod() {
        if (IOUring.isAvailable()) return TransportMethod.IO_URING;
        if (Epoll.isAvailable()) return TransportMethod.EPOLL;
        return TransportMethod.NIO;
    }
}
