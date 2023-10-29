package com.github.steveice10.packetlib.helper;

import io.netty.channel.Channel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueDatagramChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.incubator.channel.uring.IOUring;
import io.netty.incubator.channel.uring.IOUringDatagramChannel;
import io.netty.incubator.channel.uring.IOUringSocketChannel;

public class TransportHelper {
    public static final Class<? extends Channel> CHANNEL_CLASS;
    public static final Class<? extends DatagramChannel> DATAGRAM_CHANNEL_CLASS;

    static {
        var transportMethod = determineTransportMethod();
        CHANNEL_CLASS = switch (transportMethod) {
            case IO_URING -> IOUringSocketChannel.class;
            case EPOLL -> EpollSocketChannel.class;
            case KQUEUE -> KQueueSocketChannel.class;
            case NIO -> NioSocketChannel.class;
        };
        DATAGRAM_CHANNEL_CLASS = switch (transportMethod) {
            case IO_URING -> IOUringDatagramChannel.class;
            case EPOLL -> EpollDatagramChannel.class;
            case KQUEUE -> KQueueDatagramChannel.class;
            case NIO -> NioDatagramChannel.class;
        };
    }

    private TransportHelper() {
    }

    public static TransportMethod determineTransportMethod() {
        if (isClassAvailable("io.netty.incubator.channel.uring.IOUring") && IOUring.isAvailable())
            return TransportMethod.IO_URING;
        if (isClassAvailable("io.netty.channel.epoll.Epoll") && Epoll.isAvailable()) return TransportMethod.EPOLL;
        if (isClassAvailable("io.netty.channel.kqueue.KQueue") && KQueue.isAvailable()) return TransportMethod.KQUEUE;
        return TransportMethod.NIO;
    }

    /**
     * Used so implementations can opt to remove these dependencies if so desired.
     *
     * @param className The class name to check for.
     * @return Whether the class is available.
     */
    private static boolean isClassAvailable(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public enum TransportMethod {
        NIO, EPOLL, KQUEUE, IO_URING
    }
}
