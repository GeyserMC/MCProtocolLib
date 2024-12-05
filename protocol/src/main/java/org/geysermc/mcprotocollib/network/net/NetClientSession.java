package org.geysermc.mcprotocollib.network.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.mcprotocollib.network.BuiltinFlags;
import org.geysermc.mcprotocollib.network.ClientSession;
import org.geysermc.mcprotocollib.network.ProxyInfo;
import org.geysermc.mcprotocollib.network.codec.PacketCodecHelper;
import org.geysermc.mcprotocollib.network.helper.NettyHelper;
import org.geysermc.mcprotocollib.network.helper.TransportHelper;
import org.geysermc.mcprotocollib.network.packet.PacketProtocol;

import java.net.SocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class NetClientSession extends NetSession implements ClientSession {
    private static EventLoopGroup EVENT_LOOP_GROUP;

    /**
     * See {@link EventLoopGroup#shutdownGracefully(long, long, TimeUnit)}
     */
    private static final int SHUTDOWN_QUIET_PERIOD_MS = 100;
    private static final int SHUTDOWN_TIMEOUT_MS = 500;

    protected final SocketAddress bindAddress;
    protected final ProxyInfo proxy;
    protected final PacketCodecHelper codecHelper;

    public NetClientSession(SocketAddress remoteAddress, PacketProtocol protocol) {
        this(remoteAddress, null, protocol);
    }

    public NetClientSession(SocketAddress remoteAddress, PacketProtocol protocol, ProxyInfo proxy) {
        this(remoteAddress, null, protocol, proxy);
    }

    public NetClientSession(SocketAddress remoteAddress, SocketAddress bindAddress, PacketProtocol protocol) {
        this(remoteAddress, bindAddress, protocol, null);
    }

    public NetClientSession(SocketAddress remoteAddress, SocketAddress bindAddress, PacketProtocol protocol, ProxyInfo proxy) {
        this(remoteAddress, bindAddress, protocol, proxy, DefaultPacketHandlerExecutor.createExecutor());
    }

    public NetClientSession(SocketAddress remoteAddress, SocketAddress bindAddress, PacketProtocol protocol, ProxyInfo proxy, Executor packetHandlerExecutor) {
        super(remoteAddress, protocol, packetHandlerExecutor);
        this.bindAddress = bindAddress;
        this.proxy = proxy;
        this.codecHelper = protocol.createHelper();
    }

    @Override
    public void connect(boolean wait) {
        if (this.disconnected) {
            throw new IllegalStateException("Session has already been disconnected.");
        }

        final Bootstrap bootstrap = new Bootstrap()
            .channelFactory(getChannelFactory())
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, getFlag(BuiltinFlags.CLIENT_CONNECT_TIMEOUT, 30) * 1000)
            .group(getEventLoopGroup())
            .remoteAddress(remoteAddress)
            .localAddress(bindAddress)
            .handler(getChannelHandler());

        setOptions(bootstrap);

        CompletableFuture<Void> handleFuture = new CompletableFuture<>();
        bootstrap.connect().addListener((futureListener) -> {
            if (!futureListener.isSuccess()) {
                exceptionCaught(null, futureListener.cause());
            }

            handleFuture.complete(null);
        });

        if (wait) {
            handleFuture.join();
        }
    }

    @Override
    public PacketCodecHelper getCodecHelper() {
        return this.codecHelper;
    }

    protected EventLoopGroup getEventLoopGroup() {
        createEventLoopGroup();

        return EVENT_LOOP_GROUP;
    }

    protected ChannelFactory<? extends Channel> getChannelFactory() {
        return TransportHelper.TRANSPORT_TYPE.socketChannelFactory();
    }

    protected void setOptions(Bootstrap bootstrap) {
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.IP_TOS, 0x18);

        if (getFlag(BuiltinFlags.TCP_FAST_OPEN, false) && TransportHelper.TRANSPORT_TYPE.supportsTcpFastOpenClient()) {
            bootstrap.option(ChannelOption.TCP_FASTOPEN_CONNECT, true);
        }
    }

    protected ChannelHandler getChannelHandler() {
        return new MinecraftChannelInitializer<>(channel -> {
            PacketProtocol protocol = getPacketProtocol();
            protocol.newClientSession(NetClientSession.this);

            return NetClientSession.this;
        }, true) {
            @Override
            public void initChannel(@NonNull Channel channel) throws Exception {
                NettyHelper.addProxy(proxy, channel.pipeline());

                NettyHelper.initializeHAProxySupport(NetClientSession.this, channel);

                super.initChannel(channel);
            }
        };
    }

    private static void createEventLoopGroup() {
        if (EVENT_LOOP_GROUP != null) {
            return;
        }

        EVENT_LOOP_GROUP = TransportHelper.TRANSPORT_TYPE.eventLoopGroupFactory().apply(newThreadFactory());

        Runtime.getRuntime().addShutdownHook(new Thread(
            () -> EVENT_LOOP_GROUP.shutdownGracefully(SHUTDOWN_QUIET_PERIOD_MS, SHUTDOWN_TIMEOUT_MS, TimeUnit.MILLISECONDS)));
    }

    protected static ThreadFactory newThreadFactory() {
        // Create a new daemon thread. When the last non daemon thread ends
        // the runtime environment will call the shutdown hooks. One of the
        // hooks will try to shut down the event loop group which will
        // normally lead to the thread exiting. If not, it will be forcibly
        // killed after SHUTDOWN_TIMEOUT_MS along with the other
        // daemon threads as the runtime exits.
        return new DefaultThreadFactory(NetClientSession.class, true);
    }
}
