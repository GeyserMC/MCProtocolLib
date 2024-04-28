package org.geysermc.mcprotocollib.network.helper;

import io.netty.channel.epoll.Epoll;
import io.netty.channel.kqueue.KQueue;
import io.netty.incubator.channel.uring.IOUring;

public class TransportHelper {
    public enum TransportMethod {
        NIO, EPOLL, KQUEUE, IO_URING
    }

    public static TransportMethod determineTransportMethod() {
        if (isClassAvailable("io.netty.incubator.channel.uring.IOUring") && IOUring.isAvailable())
            return TransportMethod.IO_URING;
        if (isClassAvailable("io.netty.channel.epoll.Epoll") && Epoll.isAvailable()) return TransportMethod.EPOLL;
        if (isClassAvailable("io.netty.channel.kqueue.KQueue") && KQueue.isAvailable()) return TransportMethod.KQUEUE;
        return TransportMethod.NIO;
    }

    /**
     * Used so implementations can opt to remove these dependencies if so desired
     */
    private static boolean isClassAvailable(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
