package org.geysermc.mcprotocollib.network.helper;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.haproxy.HAProxyCommand;
import io.netty.handler.codec.haproxy.HAProxyMessage;
import io.netty.handler.codec.haproxy.HAProxyMessageEncoder;
import io.netty.handler.codec.haproxy.HAProxyProtocolVersion;
import io.netty.handler.codec.haproxy.HAProxyProxiedProtocol;
import io.netty.handler.proxy.HttpProxyHandler;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.network.BuiltinFlags;
import org.geysermc.mcprotocollib.network.ProxyInfo;
import org.geysermc.mcprotocollib.network.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.net.IDN;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;

public class NettyHelper {
    private static final Logger log = LoggerFactory.getLogger(NettyHelper.class);
    public static final int MC_JAVA_DEFAULT_PORT = 25565;
    private static final DirContext DIR_CONTEXT;

    static {
        try {
            String contextFactory = "com.sun.jndi.dns.DnsContextFactory";
            Class.forName(contextFactory);
            Hashtable<String, String> environment = new Hashtable<>();
            environment.put("java.naming.factory.initial", contextFactory);
            environment.put("java.naming.provider.url", "dns:");
            environment.put("com.sun.jndi.dns.timeout.retries", "1");
            DIR_CONTEXT = new InitialDirContext(environment);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static @NonNull SocketAddress resolveAddress(Session session, SocketAddress address) {
        if (address instanceof InetSocketAddress inetAddress && inetAddress.isUnresolved()) {
            SocketAddress resolved = resolveAddress(session, inetAddress.getHostString(), inetAddress.getPort());
            if (resolved != null) {
                return resolved;
            }
        }

        return address;
    }

    public static @Nullable SocketAddress resolveAddress(Session session, String host, int port) {
        ServerAddress serverAddress = ServerAddress.fromStringAndPort(host, port);

        if (session.getFlag(BuiltinFlags.ATTEMPT_SRV_RESOLVE, true) && serverAddress.port() == MC_JAVA_DEFAULT_PORT) {
            // SRVs can override address on Java, but not Bedrock.
            SocketAddress resolved = resolveSrv(session, serverAddress);
            if (resolved != null) {
                return resolved;
            }
        } else {
            log.debug("Not resolving SRV record for {}", serverAddress.host());
        }

        return resolveByHost(serverAddress);
    }

    private static @Nullable SocketAddress resolveSrv(Session session, ServerAddress serverAddress) {
        String name = session.getPacketProtocol().getSRVRecordPrefix() + "._tcp." + serverAddress.host();
        log.debug("Attempting SRV lookup for \"{}\".", name);

        try {
            Attribute srvAttribute = DIR_CONTEXT.getAttributes(name, new String[]{"SRV"}).get("srv");
            if (srvAttribute != null) {
                String[] attributeSplit = srvAttribute.get().toString().split(" ", 4);
                log.debug("SRV lookup resolved \"{}\" to \"{}\".", name, srvAttribute.get().toString());

                return resolveByHost(
                    ServerAddress.fromStringAndPort(attributeSplit[3], parseJavaPort(attributeSplit[2])));
            } else {
                log.debug("SRV lookup for \"{}\" returned no records.", name);
            }
        } catch (Exception e) {
            log.debug("Failed to resolve SRV record.", e);
        }

        return null;
    }

    private static @Nullable SocketAddress resolveByHost(ServerAddress serverAddress) {
        try {
            String host = serverAddress.host();
            InetAddress resolved = InetAddress.getByName(host);
            log.debug("Resolved {} -> {}", host, resolved.getHostAddress());
            return new InetSocketAddress(resolved, serverAddress.port());
        } catch (UnknownHostException e) {
            log.debug("Failed to resolve host.", e);
            return null;
        }
    }

    private static int parseJavaPort(String port) {
        try {
            return Integer.parseInt(port);
        } catch (NumberFormatException e) {
            return MC_JAVA_DEFAULT_PORT;
        }
    }

    public static void initializeHAProxySupport(Session session, Channel channel) {
        InetSocketAddress clientAddress = session.getFlag(BuiltinFlags.CLIENT_PROXIED_ADDRESS);
        if (clientAddress == null) {
            return;
        }

        channel.pipeline().addLast("proxy-protocol-encoder", HAProxyMessageEncoder.INSTANCE);
        channel.pipeline().addLast("proxy-protocol-packet-sender", new ChannelInboundHandlerAdapter() {
            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                HAProxyProxiedProtocol proxiedProtocol = getProxiedProtocol(clientAddress);

                SocketAddress remoteAddress = ctx.channel().remoteAddress();
                String destinationAddress;
                int destinationPort;
                if (remoteAddress instanceof InetSocketAddress inetRemoteAddress && getProxiedProtocol(inetRemoteAddress) == proxiedProtocol) {
                    destinationAddress = inetRemoteAddress.getAddress().getHostAddress();
                    destinationPort = inetRemoteAddress.getPort();
                } else {
                    // Fill in arbitrary values for the destination address and port if the remote address is not of the same type
                    switch (proxiedProtocol) {
                        case TCP4 -> {
                            destinationAddress = "0.0.0.0";
                            destinationPort = 0;
                        }
                        case TCP6 -> {
                            destinationAddress = "0:0:0:0:0:0:0:0";
                            destinationPort = 0;
                        }
                        default -> throw new UnsupportedOperationException("Unsupported proxied protocol: " + proxiedProtocol);
                    }

                    log.debug("Remote address {} is not of the same type as the client address {} - using arbitrary values for destination address and port", remoteAddress, clientAddress);
                }

                ctx.channel().writeAndFlush(new HAProxyMessage(
                    HAProxyProtocolVersion.V2, HAProxyCommand.PROXY, proxiedProtocol,
                    clientAddress.getAddress().getHostAddress(), destinationAddress,
                    clientAddress.getPort(), destinationPort
                )).addListener(future -> channel.pipeline().remove("proxy-protocol-encoder"));
                ctx.pipeline().remove(this);

                super.channelActive(ctx);
            }
        });
    }

    private static HAProxyProxiedProtocol getProxiedProtocol(InetSocketAddress socketAddress) {
        if (socketAddress.getAddress() instanceof Inet4Address) {
            return HAProxyProxiedProtocol.TCP4;
        } else if (socketAddress.getAddress() instanceof Inet6Address) {
            return HAProxyProxiedProtocol.TCP6;
        } else {
            return HAProxyProxiedProtocol.UNKNOWN;
        }
    }

    public static void addProxy(ProxyInfo proxy, ChannelPipeline pipeline) {
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

    private record ServerAddress(String internalHost, int internalPort) {
        public static ServerAddress fromStringAndPort(String host, int port) {
            return new ServerAddress(host, port);
        }

        public String host() {
            try {
                return IDN.toASCII(internalHost);
            } catch (IllegalArgumentException e) {
                return "";
            }
        }

        public int port() {
            return internalPort;
        }
    }
}
