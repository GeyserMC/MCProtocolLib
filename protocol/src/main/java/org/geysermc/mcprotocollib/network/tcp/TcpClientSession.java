package org.geysermc.mcprotocollib.network.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.codec.dns.DefaultDnsQuestion;
import io.netty.handler.codec.dns.DefaultDnsRawRecord;
import io.netty.handler.codec.dns.DefaultDnsRecordDecoder;
import io.netty.handler.codec.dns.DnsRecordType;
import io.netty.handler.codec.dns.DnsResponse;
import io.netty.handler.codec.dns.DnsSection;
import io.netty.handler.codec.haproxy.HAProxyCommand;
import io.netty.handler.codec.haproxy.HAProxyMessage;
import io.netty.handler.codec.haproxy.HAProxyMessageEncoder;
import io.netty.handler.codec.haproxy.HAProxyProtocolVersion;
import io.netty.handler.codec.haproxy.HAProxyProxiedProtocol;
import io.netty.handler.proxy.HttpProxyHandler;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.resolver.dns.DnsNameResolver;
import io.netty.resolver.dns.DnsNameResolverBuilder;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.geysermc.mcprotocollib.network.BuiltinFlags;
import org.geysermc.mcprotocollib.network.ProxyInfo;
import org.geysermc.mcprotocollib.network.codec.PacketCodecHelper;
import org.geysermc.mcprotocollib.network.helper.TransportHelper;
import org.geysermc.mcprotocollib.network.packet.PacketProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class TcpClientSession extends TcpSession {
    private static final TransportHelper.TransportType TRANSPORT_TYPE = TransportHelper.determineTransportMethod();
    private static final String IP_REGEX = "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b";
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
            .channelFactory(TRANSPORT_TYPE.socketChannelFactory())
            .option(ChannelOption.TCP_NODELAY, true)
            .option(ChannelOption.IP_TOS, 0x18)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, getFlag(BuiltinFlags.CLIENT_CONNECT_TIMEOUT, 30) * 1000)
            .group(EVENT_LOOP_GROUP)
            .remoteAddress(resolveAddress())
            .localAddress(bindAddress, bindPort)
            .handler(new ChannelInitializer<>() {
                @Override
                public void initChannel(Channel channel) {
                    PacketProtocol protocol = getPacketProtocol();
                    protocol.newClientSession(TcpClientSession.this, transferring);

                    ChannelPipeline pipeline = channel.pipeline();

                    addProxy(pipeline);

                    initializeHAProxySupport(channel);

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

        if (getFlag(BuiltinFlags.TCP_FAST_OPEN, false) && TRANSPORT_TYPE.supportsTcpFastOpenClient()) {
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

    private InetSocketAddress resolveAddress() {
        String name = this.getPacketProtocol().getSRVRecordPrefix() + "._tcp." + this.getHost();
        log.debug("Attempting SRV lookup for \"{}\".", name);

        if (getFlag(BuiltinFlags.ATTEMPT_SRV_RESOLVE, true) && (!this.host.matches(IP_REGEX) && !this.host.equalsIgnoreCase("localhost"))) {
            AddressedEnvelope<DnsResponse, InetSocketAddress> envelope = null;
            try (DnsNameResolver resolver = new DnsNameResolverBuilder(EVENT_LOOP_GROUP.next())
                .channelFactory(TRANSPORT_TYPE.datagramChannelFactory())
                .build()) {
                envelope = resolver.query(new DefaultDnsQuestion(name, DnsRecordType.SRV)).get();

                DnsResponse response = envelope.content();
                if (response.count(DnsSection.ANSWER) > 0) {
                    DefaultDnsRawRecord record = response.recordAt(DnsSection.ANSWER, 0);
                    if (record.type() == DnsRecordType.SRV) {
                        ByteBuf buf = record.content();
                        buf.skipBytes(4); // Skip priority and weight.

                        int port = buf.readUnsignedShort();
                        String host = DefaultDnsRecordDecoder.decodeName(buf);
                        if (host.endsWith(".")) {
                            host = host.substring(0, host.length() - 1);
                        }

                        log.debug("Found SRV record containing \"{}:{}\".", host, port);

                        this.host = host;
                        this.port = port;
                    } else {
                        log.debug("Received non-SRV record in response.");
                    }
                } else {
                    log.debug("No SRV record found.");
                }
            } catch (Exception e) {
                log.debug("Failed to resolve SRV record.", e);
            } finally {
                if (envelope != null) {
                    envelope.release();
                }

            }
        } else {
            log.debug("Not resolving SRV record for {}", this.host);
        }

        // Resolve host here
        try {
            InetAddress resolved = InetAddress.getByName(getHost());
            log.debug("Resolved {} -> {}", getHost(), resolved.getHostAddress());
            return new InetSocketAddress(resolved, getPort());
        } catch (UnknownHostException e) {
            log.debug("Failed to resolve host, letting Netty do it instead.", e);
            return InetSocketAddress.createUnresolved(getHost(), getPort());
        }
    }

    private void addProxy(ChannelPipeline pipeline) {
        if (proxy == null) {
            return;
        }

        switch (proxy.type()) {
            case HTTP -> {
                if (proxy.username() != null && proxy.password() != null) {
                    pipeline.addLast("proxy", new HttpProxyHandler(proxy.address(), proxy.username(), proxy.password()));
                } else {
                    pipeline.addLast("proxy", new HttpProxyHandler(proxy.address()));
                }
            }
            case SOCKS4 -> {
                if (proxy.username() != null) {
                    pipeline.addLast("proxy", new Socks4ProxyHandler(proxy.address(), proxy.username()));
                } else {
                    pipeline.addLast("proxy", new Socks4ProxyHandler(proxy.address()));
                }
            }
            case SOCKS5 -> {
                if (proxy.username() != null && proxy.password() != null) {
                    pipeline.addLast("proxy", new Socks5ProxyHandler(proxy.address(), proxy.username(), proxy.password()));
                } else {
                    pipeline.addLast("proxy", new Socks5ProxyHandler(proxy.address()));
                }
            }
            default -> throw new UnsupportedOperationException("Unsupported proxy type: " + proxy.type());
        }
    }

    private void initializeHAProxySupport(Channel channel) {
        InetSocketAddress clientAddress = getFlag(BuiltinFlags.CLIENT_PROXIED_ADDRESS);
        if (clientAddress == null) {
            return;
        }

        channel.pipeline().addLast("proxy-protocol-encoder", HAProxyMessageEncoder.INSTANCE);
        HAProxyProxiedProtocol proxiedProtocol = clientAddress.getAddress() instanceof Inet4Address ? HAProxyProxiedProtocol.TCP4 : HAProxyProxiedProtocol.TCP6;
        InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
        channel.writeAndFlush(new HAProxyMessage(
            HAProxyProtocolVersion.V2, HAProxyCommand.PROXY, proxiedProtocol,
            clientAddress.getAddress().getHostAddress(), remoteAddress.getAddress().getHostAddress(),
            clientAddress.getPort(), remoteAddress.getPort()
        )).addListener(future -> channel.pipeline().remove("proxy-protocol-encoder"));
    }

    private static void createTcpEventLoopGroup() {
        if (EVENT_LOOP_GROUP != null) {
            return;
        }

        EVENT_LOOP_GROUP = TRANSPORT_TYPE.eventLoopGroupFactory().apply(newThreadFactory());

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
