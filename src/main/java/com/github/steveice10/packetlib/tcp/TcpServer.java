package com.github.steveice10.packetlib.tcp;

import com.github.steveice10.packetlib.AbstractServer;
import com.github.steveice10.packetlib.BuiltinFlags;
import com.github.steveice10.packetlib.packet.PacketProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.incubator.channel.uring.IOUring;
import io.netty.incubator.channel.uring.IOUringEventLoopGroup;
import io.netty.incubator.channel.uring.IOUringServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;

public class TcpServer extends AbstractServer {
    private EventLoopGroup group;
    private Class<? extends ServerSocketChannel> serverSocketChannel;
    private Channel channel;

    public TcpServer(String host, int port, Class<? extends PacketProtocol> protocol) {
        super(host, port, protocol);
    }

    @Override
    public boolean isListening() {
        return this.channel != null && this.channel.isOpen();
    }

    public enum TransportMethod {
        NIO, EPOLL, IO_URING;
    }

    private TransportMethod determineTransportMethod() {
        if (IOUring.isAvailable()) return TransportMethod.IO_URING;
        if (Epoll.isAvailable()) return TransportMethod.EPOLL;
        return TransportMethod.NIO;
    }

    @Override
    public void bindImpl(boolean wait, final Runnable callback) {
        if(this.group != null || this.channel != null) {
            return;
        }

        switch (determineTransportMethod()) {
            case IO_URING:
                this.group = new IOUringEventLoopGroup();
                this.serverSocketChannel = IOUringServerSocketChannel.class;
                break;
            case EPOLL:
                this.group = new EpollEventLoopGroup();
                this.serverSocketChannel = EpollServerSocketChannel.class;
                break;
            case NIO:
                this.group = new NioEventLoopGroup();
                this.serverSocketChannel = NioServerSocketChannel.class;
                break;
        }

        ChannelFuture future = new ServerBootstrap().channel(this.serverSocketChannel).childHandler(new ChannelInitializer<Channel>() {
            @Override
            public void initChannel(Channel channel) throws Exception {
                InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
                PacketProtocol protocol = createPacketProtocol();

                TcpSession session = new TcpServerSession(address.getHostName(), address.getPort(), protocol, TcpServer.this);
                session.getPacketProtocol().newServerSession(TcpServer.this, session);

                channel.config().setOption(ChannelOption.IP_TOS, 0x18);
                channel.config().setOption(ChannelOption.TCP_NODELAY, false);

                ChannelPipeline pipeline = channel.pipeline();

                session.refreshReadTimeoutHandler(channel);
                session.refreshWriteTimeoutHandler(channel);

                pipeline.addLast("encryption", new TcpPacketEncryptor(session));
                pipeline.addLast("sizer", new TcpPacketSizer(session));
                pipeline.addLast("codec", new TcpPacketCodec(session));
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
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()) {
                        channel = future.channel();
                        if(callback != null) {
                            callback.run();
                        }
                    } else {
                        System.err.println("[ERROR] Failed to asynchronously bind connection listener.");
                        if(future.cause() != null) {
                            future.cause().printStackTrace();
                        }
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
                future.addListener(new GenericFutureListener() {
                    @Override
                    public void operationComplete(Future future) {
                        if(!future.isSuccess() && getGlobalFlag(BuiltinFlags.PRINT_DEBUG, false)) {
                            System.err.println("[ERROR] Failed to asynchronously close connection listener.");
                            if(future.cause() != null) {
                                future.cause().printStackTrace();
                            }
                        }
                    }
                });
            }

            this.group = null;
        }
    }
}
