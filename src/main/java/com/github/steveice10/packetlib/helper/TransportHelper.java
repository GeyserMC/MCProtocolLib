package com.github.steveice10.packetlib.helper;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.*;
import io.netty.channel.kqueue.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.incubator.channel.uring.*;

import java.util.concurrent.ThreadFactory;

public class TransportHelper {
    public static final Class<? extends SocketChannel> SOCKET_CHANNEL_CLASS;
    public static final Class<? extends DatagramChannel> DATAGRAM_CHANNEL_CLASS;
    public static final Class<? extends ServerSocketChannel> SERVER_SOCKET_CHANNEL_CLASS;

    static {
        var transportMethod = determineTransportMethod();
        SOCKET_CHANNEL_CLASS = switch (transportMethod) {
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
        SERVER_SOCKET_CHANNEL_CLASS = switch (transportMethod) {
            case IO_URING -> IOUringServerSocketChannel.class;
            case EPOLL -> EpollServerSocketChannel.class;
            case KQUEUE -> KQueueServerSocketChannel.class;
            case NIO -> NioServerSocketChannel.class;
        };
    }

    private TransportHelper() {
    }

    public static TransportMethod determineTransportMethod() {
        if (isClassAvailable("io.netty.incubator.channel.uring.IOUring") && IOUring.isAvailable()) {
            return TransportMethod.IO_URING;
        } else if (isClassAvailable("io.netty.channel.epoll.Epoll") && Epoll.isAvailable()) {
            return TransportMethod.EPOLL;
        } else if (isClassAvailable("io.netty.channel.kqueue.KQueue") && KQueue.isAvailable()) {
            return TransportMethod.KQUEUE;
        } else {
            return TransportMethod.NIO;
        }
    }

    public static EventLoopGroup createEventLoopGroup() {
        return switch (determineTransportMethod()) {
            case IO_URING -> new IOUringEventLoopGroup();
            case EPOLL -> new EpollEventLoopGroup();
            case KQUEUE -> new KQueueEventLoopGroup();
            case NIO -> new NioEventLoopGroup();
        };
    }

    public static EventLoopGroup createEventLoopGroup(ThreadFactory threadFactory) {
        return switch (determineTransportMethod()) {
            case IO_URING -> new IOUringEventLoopGroup(threadFactory);
            case EPOLL -> new EpollEventLoopGroup(threadFactory);
            case KQUEUE -> new KQueueEventLoopGroup(threadFactory);
            case NIO -> new NioEventLoopGroup(threadFactory);
        };
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
