package org.geysermc.mcprotocollib.network.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.mcprotocollib.network.AbstractServer;
import org.geysermc.mcprotocollib.network.BuiltinFlags;
import org.geysermc.mcprotocollib.network.helper.TransportHelper;
import org.geysermc.mcprotocollib.network.packet.PacketProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class NetServer extends AbstractServer {
    private static final Logger log = LoggerFactory.getLogger(NetServer.class);

    private final Supplier<Executor> packetHandlerExecutorFactory;
    private EventLoopGroup group;
    private Channel channel;

    public NetServer(SocketAddress bindAddress, Supplier<? extends PacketProtocol> protocol) {
        this(bindAddress, protocol, DefaultPacketHandlerExecutor::createExecutor);
    }

    public NetServer(SocketAddress bindAddress, Supplier<? extends PacketProtocol> protocol, Supplier<Executor> packetHandlerExecutorFactory) {
        super(bindAddress, protocol);
        this.packetHandlerExecutorFactory = packetHandlerExecutorFactory;
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

        this.group = TransportHelper.TRANSPORT_TYPE.eventLoopGroupFactory().apply(null);

        ServerBootstrap bootstrap = new ServerBootstrap()
                .channelFactory(getChannelFactory())
                .group(this.group)
                .localAddress(this.getBindAddress())
                .childHandler(getChannelHandler());

        setOptions(bootstrap);

        CompletableFuture<Void> handleFuture = new CompletableFuture<>();
        bootstrap.bind().addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                channel = future.channel();
                if (callback != null) {
                    callback.run();
                }
            } else {
                log.error("Failed to bind connection listener.", future.cause());
            }

            handleFuture.complete(null);
        });

        if (wait) {
            handleFuture.join();
        }
    }

    protected ChannelFactory<? extends ServerSocketChannel> getChannelFactory() {
        return TransportHelper.TRANSPORT_TYPE.serverSocketChannelFactory();
    }

    protected void setOptions(ServerBootstrap bootstrap) {
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        bootstrap.childOption(ChannelOption.IP_TOS, 0x18);

        if (getGlobalFlag(BuiltinFlags.TCP_FAST_OPEN, false) && TransportHelper.TRANSPORT_TYPE.supportsTcpFastOpenServer()) {
            bootstrap.option(ChannelOption.TCP_FASTOPEN, 3);
        }
    }

    protected ChannelHandler getChannelHandler() {
        return new ChannelInitializer<>() {
            @Override
            public void initChannel(@NonNull Channel channel) {
                PacketProtocol protocol = createPacketProtocol();

                NetSession session = new NetServerSession(channel.remoteAddress(), protocol, NetServer.this, packetHandlerExecutorFactory.get());
                session.getPacketProtocol().newServerSession(NetServer.this, session);

                ChannelPipeline pipeline = channel.pipeline();

                pipeline.addLast("read-timeout", new ReadTimeoutHandler(session.getFlag(BuiltinFlags.READ_TIMEOUT, 30)));
                pipeline.addLast("write-timeout", new WriteTimeoutHandler(session.getFlag(BuiltinFlags.WRITE_TIMEOUT, 0)));

                pipeline.addLast("encryption", new NetPacketEncryptor());
                pipeline.addLast("sizer", new NetPacketSizer(protocol.getPacketHeader(), session.getCodecHelper()));
                pipeline.addLast("compression", new NetPacketCompression(session.getCodecHelper()));

                pipeline.addLast("flow-control", new NetFlowControlHandler());
                pipeline.addLast("codec", new NetPacketCodec(session, false));
                pipeline.addLast("flush-handler", new FlushHandler());
                pipeline.addLast("manager", session);
            }
        };
    }

    @Override
    public void closeImpl(boolean wait, final Runnable callback) {
        if (this.channel != null) {
            if (this.channel.isOpen()) {
                CompletableFuture<Void> handleFuture = new CompletableFuture<>();
                this.channel.close().addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        if (callback != null) {
                            callback.run();
                        }
                    } else {
                        log.error("Failed to close connection listener.", future.cause());
                    }

                    handleFuture.complete(null);
                });

                if (wait) {
                    handleFuture.join();
                }
            }

            this.channel = null;
        }

        if (this.group != null) {
            CompletableFuture<Void> handleFuture = new CompletableFuture<>();
            this.group.shutdownGracefully().addListener(future -> {
                if (!future.isSuccess()) {
                    log.debug("Failed to close connection listener.", future.cause());
                }

                handleFuture.complete(null);
            });

            if (wait) {
                handleFuture.join();
            }

            this.group = null;
        }
    }
}
