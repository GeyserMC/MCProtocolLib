package org.geysermc.mcprotocollib.network.helper;

import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueDatagramChannel;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.uring.IOUring;
import io.netty.channel.uring.IOUringDatagramChannel;
import io.netty.channel.uring.IOUringEventLoopGroup;
import io.netty.channel.uring.IOUringServerSocketChannel;
import io.netty.channel.uring.IOUringSocketChannel;

import java.util.concurrent.ThreadFactory;
import java.util.function.Function;

public class TransportHelper {
    public static final TransportHelper.TransportType TRANSPORT_TYPE = TransportHelper.determineTransportMethod();

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
                                Function<ThreadFactory, EventLoopGroup> eventLoopGroupFactory,
                                boolean supportsTcpFastOpenServer,
                                boolean supportsTcpFastOpenClient) {
    }

    private static TransportType determineTransportMethod() {
        if (isClassAvailable("io.netty.incubator.channel.uring.IOUring") && IOUring.isAvailable()) {
            return new TransportType(
                    TransportMethod.IO_URING,
                    IOUringServerSocketChannel.class,
                    IOUringServerSocketChannel::new,
                    IOUringSocketChannel.class,
                    IOUringSocketChannel::new,
                    IOUringDatagramChannel.class,
                    IOUringDatagramChannel::new,
                    factory -> new IOUringEventLoopGroup(0, factory),
                    IOUring.isTcpFastOpenServerSideAvailable(),
                    IOUring.isTcpFastOpenClientSideAvailable()
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
                    factory -> new EpollEventLoopGroup(0, factory),
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
                    factory -> new KQueueEventLoopGroup(0, factory),
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
                factory -> new NioEventLoopGroup(0, factory),
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
