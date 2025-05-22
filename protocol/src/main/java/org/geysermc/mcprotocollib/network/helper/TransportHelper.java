package org.geysermc.mcprotocollib.network.helper;

import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollIoHandler;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueDatagramChannel;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueIoHandler;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.uring.IoUring;
import io.netty.channel.uring.IoUringDatagramChannel;
import io.netty.channel.uring.IoUringIoHandler;
import io.netty.channel.uring.IoUringServerSocketChannel;
import io.netty.channel.uring.IoUringSocketChannel;

import java.util.concurrent.ThreadFactory;
import java.util.function.BiFunction;

public class TransportHelper {
    public static final TransportHelper.TransportType TRANSPORT_TYPE = TransportHelper.determineTransportMethod();
    public static final boolean NEW_NETTY = isClassAvailable("io.netty.channel.MultiThreadIoEventLoopGroup");

    public enum TransportMethod {
        NIO, EPOLL, KQUEUE, IO_URING
    }

    public record TransportType(TransportMethod method,
                                Class<? extends ServerSocketChannel> serverSocketChannelClass,
                                ChannelFactory<? extends ServerSocketChannel> serverSocketChannelFactory,
                                Class<? extends SocketChannel> socketChannelClass,
                                ChannelFactory<? extends SocketChannel> socketChannelFactory,
                                Class<? extends DatagramChannel> datagramChannelClass,
                                ChannelFactory<? extends DatagramChannel> datagramChannelFactory,
                                BiFunction<Integer, ThreadFactory, EventLoopGroup> eventLoopGroupFactory,
                                boolean supportsTcpFastOpenServer,
                                boolean supportsTcpFastOpenClient) {
    }

    @SuppressWarnings("deprecation")
    private static TransportType determineTransportMethod() {
        if (isClassAvailable("io.netty.channel.uring.IoUring")
            && IoUring.isAvailable()
            && Boolean.getBoolean("Mcpl.io_uring")
        ) {
            return new TransportType(
                    TransportMethod.IO_URING,
                    IoUringServerSocketChannel.class,
                    IoUringServerSocketChannel::new,
                    IoUringSocketChannel.class,
                    IoUringSocketChannel::new,
                    IoUringDatagramChannel.class,
                    IoUringDatagramChannel::new,
                    (threads, factory) -> new MultiThreadIoEventLoopGroup(threads, factory, IoUringIoHandler.newFactory()),
                    IoUring.isTcpFastOpenServerSideAvailable(),
                    IoUring.isTcpFastOpenClientSideAvailable()
            );
        }

        if (isClassAvailable("io.netty.channel.epoll.Epoll") && Epoll.isAvailable()) {
            return new TransportType(
                    TransportMethod.EPOLL,
                    EpollServerSocketChannel.class,
                    EpollServerSocketChannel::new,
                    EpollSocketChannel.class,
                    EpollSocketChannel::new,
                    EpollDatagramChannel.class,
                    EpollDatagramChannel::new,
                    NEW_NETTY ? (threads, factory) ->
                        new MultiThreadIoEventLoopGroup(threads, factory, EpollIoHandler.newFactory()) : EpollEventLoopGroup::new,
                    Epoll.isTcpFastOpenServerSideAvailable(),
                    Epoll.isTcpFastOpenClientSideAvailable()
            );
        }

        if (isClassAvailable("io.netty.channel.kqueue.KQueue") && KQueue.isAvailable()) {
            return new TransportType(
                    TransportMethod.KQUEUE,
                    KQueueServerSocketChannel.class,
                    KQueueServerSocketChannel::new,
                    KQueueSocketChannel.class,
                    KQueueSocketChannel::new,
                    KQueueDatagramChannel.class,
                    KQueueDatagramChannel::new,
                    NEW_NETTY ? (threads, factory) ->
                        new MultiThreadIoEventLoopGroup(threads, factory, KQueueIoHandler.newFactory()) : KQueueEventLoopGroup::new,
                    KQueue.isTcpFastOpenServerSideAvailable(),
                    KQueue.isTcpFastOpenClientSideAvailable()
            );
        }

        return new TransportType(
                TransportMethod.NIO,
                NioServerSocketChannel.class,
                NioServerSocketChannel::new,
                NioSocketChannel.class,
                NioSocketChannel::new,
                NioDatagramChannel.class,
                NioDatagramChannel::new,
                NEW_NETTY ? (threads, factory) ->
                    new MultiThreadIoEventLoopGroup(threads, factory, NioIoHandler.newFactory()) : NioEventLoopGroup::new,
                false,
                false
        );
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
