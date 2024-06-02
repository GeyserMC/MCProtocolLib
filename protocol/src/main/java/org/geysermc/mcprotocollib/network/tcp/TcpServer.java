package org.geysermc.mcprotocollib.network.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.Future;
import org.geysermc.mcprotocollib.network.AbstractServer;
import org.geysermc.mcprotocollib.network.BuiltinFlags;
import org.geysermc.mcprotocollib.network.helper.TransportHelper;
import org.geysermc.mcprotocollib.network.packet.PacketProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.function.Supplier;

public class TcpServer extends AbstractServer {
    private static final TransportHelper.TransportType TRANSPORT_TYPE = TransportHelper.determineTransportMethod();
    private static final Logger log = LoggerFactory.getLogger(TcpServer.class);

    private EventLoopGroup group;
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
        if (this.group != null || this.channel != null) {
            return;
        }

        this.group = TRANSPORT_TYPE.eventLoopGroupFactory().apply(null);

        ServerBootstrap bootstrap = new ServerBootstrap()
                .channelFactory(TRANSPORT_TYPE.serverSocketChannelFactory())
                .group(this.group)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.IP_TOS, 0x18)
                .localAddress(this.getHost(), this.getPort())
                .childHandler(new ChannelInitializer<>() {
            @Override
            public void initChannel(Channel channel) {
                InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
                PacketProtocol protocol = createPacketProtocol();

                TcpSession session = new TcpServerSession(address.getHostName(), address.getPort(), protocol, TcpServer.this);
                session.getPacketProtocol().newServerSession(TcpServer.this, session);

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
        });

        if (getGlobalFlag(BuiltinFlags.TCP_FAST_OPEN, false) && TRANSPORT_TYPE.supportsTcpFastOpenServer()) {
            bootstrap.option(ChannelOption.TCP_FASTOPEN, 3);
        }

        ChannelFuture future = bootstrap.bind();

        if (wait) {
            try {
                future.sync();
            } catch (InterruptedException e) {
            }

            channel = future.channel();
            if (callback != null) {
                callback.run();
            }
        } else {
            future.addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    channel = future1.channel();
                    if (callback != null) {
                        callback.run();
                    }
                } else {
                    log.error("Failed to asynchronously bind connection listener.", future1.cause());
                }
            });
        }
    }

    @Override
    public void closeImpl(boolean wait, final Runnable callback) {
        if (this.channel != null) {
            if (this.channel.isOpen()) {
                ChannelFuture future = this.channel.close();
                if (wait) {
                    try {
                        future.sync();
                    } catch (InterruptedException e) {
                    }

                    if (callback != null) {
                        callback.run();
                    }
                } else {
                    future.addListener((ChannelFutureListener) future1 -> {
                        if (future1.isSuccess()) {
                            if (callback != null) {
                                callback.run();
                            }
                        } else {
                            log.error("Failed to asynchronously close connection listener.", future1.cause());
                        }
                    });
                }
            }

            this.channel = null;
        }

        if (this.group != null) {
            Future<?> future = this.group.shutdownGracefully();
            if (wait) {
                try {
                    future.sync();
                } catch (InterruptedException e) {
                }
            } else {
                future.addListener(future1 -> {
                    if (!future1.isSuccess()) {
                        log.debug("Failed to asynchronously close connection listener.", future1.cause());
                    }
                });
            }

            this.group = null;
        }
    }
}
