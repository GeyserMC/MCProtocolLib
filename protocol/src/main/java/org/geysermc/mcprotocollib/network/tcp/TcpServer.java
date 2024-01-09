package org.geysermc.mcprotocollib.network.tcp;

import org.geysermc.mcprotocollib.network.AbstractServer;
import org.geysermc.mcprotocollib.network.BuiltinFlags;
import org.geysermc.mcprotocollib.network.helper.TransportHelper;
import org.geysermc.mcprotocollib.network.packet.PacketProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.*;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.incubator.channel.uring.IOUringEventLoopGroup;
import io.netty.incubator.channel.uring.IOUringServerSocketChannel;
import io.netty.util.concurrent.Future;

import java.net.InetSocketAddress;
import java.util.function.Supplier;

public class TcpServer extends AbstractServer {
    private EventLoopGroup group;
    private Class<? extends ServerSocketChannel> serverSocketChannel;
    private Channel channel;

    public TcpServer(String host, int port, Supplier<? extends PacketProtocol> protocol) {
        super(host, port, protocol);
    }

    @Override
    public boolean isListening() {
        return this.channel != null && this.channel.isOpen();
    }

    @Override
    public void bindImpl(boolean wait, final Runnable callback) {
        if(this.group != null || this.channel != null) {
            return;
        }

        switch (TransportHelper.determineTransportMethod()) {
            case IO_URING -> {
                this.group = new IOUringEventLoopGroup();
                this.serverSocketChannel = IOUringServerSocketChannel.class;
            }
            case EPOLL -> {
                this.group = new EpollEventLoopGroup();
                this.serverSocketChannel = EpollServerSocketChannel.class;
            }
            case KQUEUE -> {
                this.group = new KQueueEventLoopGroup();
                this.serverSocketChannel = KQueueServerSocketChannel.class;
            }
            case NIO -> {
                this.group = new NioEventLoopGroup();
                this.serverSocketChannel = NioServerSocketChannel.class;
            }
        }

        ChannelFuture future = new ServerBootstrap().channel(this.serverSocketChannel).childHandler(new ChannelInitializer<>() {
            @Override
            public void initChannel(Channel channel) {
                InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
                PacketProtocol protocol = createPacketProtocol();

                TcpSession session = new TcpServerSession(address.getHostName(), address.getPort(), protocol, TcpServer.this);
                session.getPacketProtocol().newServerSession(TcpServer.this, session);

                channel.config().setOption(ChannelOption.IP_TOS, 0x18);
                try {
                    channel.config().setOption(ChannelOption.TCP_NODELAY, true);
                } catch (ChannelException ignored) {
                }

                ChannelPipeline pipeline = channel.pipeline();

                session.refreshReadTimeoutHandler(channel);
                session.refreshWriteTimeoutHandler(channel);

                int size = protocol.getPacketHeader().getLengthSize();
                if (size > 0) {
                    pipeline.addLast("sizer", new TcpPacketSizer(session, size));
                }

                pipeline.addLast("codec", new TcpPacketCodec(session, false));
                pipeline.addLast("manager", session);
            }
        }).group(this.group).localAddress(this.getHost(), this.getPort()).bind();

        if(wait) {
            try {
                future.sync();
            } catch(InterruptedException e) {
            }

            channel = future.channel();
            if(callback != null) {
                callback.run();
            }
        } else {
            future.addListener((ChannelFutureListener) future1 -> {
                if(future1.isSuccess()) {
                    channel = future1.channel();
                    if(callback != null) {
                        callback.run();
                    }
                } else {
                    System.err.println("[ERROR] Failed to asynchronously bind connection listener.");
                    if(future1.cause() != null) {
                        future1.cause().printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void closeImpl(boolean wait, final Runnable callback) {
        if(this.channel != null) {
            if(this.channel.isOpen()) {
                ChannelFuture future = this.channel.close();
                if(wait) {
                    try {
                        future.sync();
                    } catch(InterruptedException e) {
                    }

                    if(callback != null) {
                        callback.run();
                    }
                } else {
                    future.addListener((ChannelFutureListener) future1 -> {
                        if(future1.isSuccess()) {
                            if(callback != null) {
                                callback.run();
                            }
                        } else {
                            System.err.println("[ERROR] Failed to asynchronously close connection listener.");
                            if(future1.cause() != null) {
                                future1.cause().printStackTrace();
                            }
                        }
                    });
                }
            }

            this.channel = null;
        }

        if(this.group != null) {
            Future<?> future = this.group.shutdownGracefully();
            if(wait) {
                try {
                    future.sync();
                } catch(InterruptedException e) {
                }
            } else {
                future.addListener(future1 -> {
                    if(!future1.isSuccess() && getGlobalFlag(BuiltinFlags.PRINT_DEBUG, false)) {
                        System.err.println("[ERROR] Failed to asynchronously close connection listener.");
                        if(future1.cause() != null) {
                            future1.cause().printStackTrace();
                        }
                    }
                });
            }

            this.group = null;
        }
    }
}
