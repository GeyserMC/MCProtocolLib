package com.github.steveice10.packetlib.tcp;

import com.github.steveice10.packetlib.ConnectionListener;
import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.packet.PacketProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;

public class TcpConnectionListener implements ConnectionListener {
    private String host;
    private int port;
    private Server server;

    private EventLoopGroup group;
    private Channel channel;

    public TcpConnectionListener(String host, int port, Server server) {
        this.host = host;
        this.port = port;
        this.server = server;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public boolean isListening() {
        return this.channel != null && this.channel.isOpen();
    }

    @Override
    public void bind() {
        this.bind(true);
    }

    @Override
    public void bind(boolean wait) {
        this.bind(wait, null);
    }

    @Override
    public void bind(boolean wait, final Runnable callback) {
        if(this.group != null || this.channel != null) {
            return;
        }

        this.group = new NioEventLoopGroup();
        ChannelFuture future = new ServerBootstrap().channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<Channel>() {
            @Override
            public void initChannel(Channel channel) throws Exception {
                InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
                PacketProtocol protocol = server.createPacketProtocol();

                TcpSession session = new TcpServerSession(address.getHostName(), address.getPort(), protocol, server);
                session.getPacketProtocol().newServerSession(server, session);

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
        }).group(this.group).localAddress(this.host, this.port).bind();

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
    public void close() {
        this.close(false);
    }

    @Override
    public void close(boolean wait) {
        this.close(wait, null);
    }

    @Override
    public void close(boolean wait, final Runnable callback) {
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
                    future.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if(future.isSuccess()) {
                                if(callback != null) {
                                    callback.run();
                                }
                            } else {
                                System.err.println("[ERROR] Failed to asynchronously close connection listener.");
                                if(future.cause() != null) {
                                    future.cause().printStackTrace();
                                }
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
                    public void operationComplete(Future future) throws Exception {
                        if(!future.isSuccess()) {
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
