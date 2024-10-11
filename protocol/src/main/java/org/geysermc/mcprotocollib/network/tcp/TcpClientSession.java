package org.geysermc.mcprotocollib.network.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.mcprotocollib.network.BuiltinFlags;
import org.geysermc.mcprotocollib.network.ProxyInfo;
import org.geysermc.mcprotocollib.network.codec.PacketCodecHelper;
import org.geysermc.mcprotocollib.network.helper.NettyHelper;
import org.geysermc.mcprotocollib.network.helper.TransportHelper;
import org.geysermc.mcprotocollib.network.packet.PacketProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class TcpClientSession extends TcpSession {
    private static final Logger log = LoggerFactory.getLogger(TcpClientSession.class);
    private static EventLoopGroup EVENT_LOOP_GROUP;

    /**
     * See {@link EventLoopGroup#shutdownGracefully(long, long, TimeUnit)}
     */
    private static final int SHUTDOWN_QUIET_PERIOD_MS = 100;
    private static final int SHUTDOWN_TIMEOUT_MS = 500;

    private final String bindAddress;
    private final int bindPort;
    private final ProxyInfo proxy;
    private final PacketCodecHelper codecHelper;

    public TcpClientSession(String host, int port, PacketProtocol protocol) {
        this(host, port, protocol, null);
    }

    public TcpClientSession(String host, int port, PacketProtocol protocol, ProxyInfo proxy) {
        this(host, port, "0.0.0.0", 0, protocol, proxy);
    }

    public TcpClientSession(String host, int port, String bindAddress, int bindPort, PacketProtocol protocol) {
        this(host, port, bindAddress, bindPort, protocol, null);
    }

    public TcpClientSession(String host, int port, String bindAddress, int bindPort, PacketProtocol protocol, ProxyInfo proxy) {
        super(host, port, protocol);
        this.bindAddress = bindAddress;
        this.bindPort = bindPort;
        this.proxy = proxy;
        this.codecHelper = protocol.createHelper();
    }

    @Override
    public void connect(boolean wait, boolean transferring) {
        if (this.disconnected) {
            throw new IllegalStateException("Session has already been disconnected.");
        }

        if (EVENT_LOOP_GROUP == null) {
            createTcpEventLoopGroup();
        }

        final Bootstrap bootstrap = new Bootstrap()
            .channelFactory(TransportHelper.TRANSPORT_TYPE.socketChannelFactory())
            .option(ChannelOption.TCP_NODELAY, true)
            .option(ChannelOption.IP_TOS, 0x18)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, getFlag(BuiltinFlags.CLIENT_CONNECT_TIMEOUT, 30) * 1000)
            .group(EVENT_LOOP_GROUP)
            .remoteAddress(NettyHelper.resolveAddress(this, EVENT_LOOP_GROUP.next(), getHost(), getPort()))
            .localAddress(bindAddress, bindPort)
            .handler(new ChannelInitializer<>() {
                @Override
                public void initChannel(@NonNull Channel channel) {
                    PacketProtocol protocol = getPacketProtocol();
                    protocol.newClientSession(TcpClientSession.this, transferring);

                    ChannelPipeline pipeline = channel.pipeline();

                    NettyHelper.addProxy(proxy, pipeline);

                    NettyHelper.initializeHAProxySupport(TcpClientSession.this, channel);

                    pipeline.addLast("read-timeout", new ReadTimeoutHandler(getFlag(BuiltinFlags.READ_TIMEOUT, 30)));
                    pipeline.addLast("write-timeout", new WriteTimeoutHandler(getFlag(BuiltinFlags.WRITE_TIMEOUT, 0)));

                    pipeline.addLast("encryption", new TcpPacketEncryptor());
                    pipeline.addLast("sizer", new TcpPacketSizer(protocol.getPacketHeader(), getCodecHelper()));
                    pipeline.addLast("compression", new TcpPacketCompression(getCodecHelper()));

                    pipeline.addLast("flow-control", new TcpFlowControlHandler());
                    pipeline.addLast("codec", new TcpPacketCodec(TcpClientSession.this, true));
                    pipeline.addLast("flush-handler", new FlushHandler());
                    pipeline.addLast("manager", TcpClientSession.this);
                }
            });

        if (getFlag(BuiltinFlags.TCP_FAST_OPEN, false) && TransportHelper.TRANSPORT_TYPE.supportsTcpFastOpenClient()) {
            bootstrap.option(ChannelOption.TCP_FASTOPEN_CONNECT, true);
        }

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

    private static void createTcpEventLoopGroup() {
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
        return new DefaultThreadFactory(TcpClientSession.class, true);
    }
}
