package org.spacehq.packetlib.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.packet.PacketProtocol;

import javax.naming.directory.InitialDirContext;
import java.net.Proxy;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicBoolean;

public class TcpClientSession extends TcpSession {
    private Client client;
    private Proxy proxy;

    private boolean connected;
    private EventLoopGroup group;

    public TcpClientSession(String host, int port, PacketProtocol protocol, Client client, Proxy proxy) {
        super(host, port, protocol);
        this.client = client;
        this.proxy = proxy;
    }

    @Override
    public void connect(boolean wait) {
        if(this.disconnected) {
            throw new IllegalStateException("Session has already been disconnected.");
        } else if(this.connected) {
            return;
        }

        this.connected = true;
        try {
            final Bootstrap bootstrap = new Bootstrap();
            if(this.proxy != null) {
                this.group = new OioEventLoopGroup();
                bootstrap.channelFactory(new ProxyOioChannelFactory(this.proxy));
            } else {
                this.group = new NioEventLoopGroup();
                bootstrap.channel(NioSocketChannel.class);
            }

            bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                public void initChannel(Channel channel) throws Exception {
                    getPacketProtocol().newClientSession(client, TcpClientSession.this);

                    channel.config().setOption(ChannelOption.IP_TOS, 0x18);
                    channel.config().setOption(ChannelOption.TCP_NODELAY, false);

                    ChannelPipeline pipeline = channel.pipeline();

                    refreshReadTimeoutHandler(channel);
                    refreshWriteTimeoutHandler(channel);

                    pipeline.addLast("encryption", new TcpPacketEncryptor(TcpClientSession.this));
                    pipeline.addLast("sizer", new TcpPacketSizer(TcpClientSession.this));
                    pipeline.addLast("codec", new TcpPacketCodec(TcpClientSession.this));
                    pipeline.addLast("manager", TcpClientSession.this);
                }
            }).group(this.group).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, getConnectTimeout() * 1000);

            final AtomicBoolean complete = new AtomicBoolean(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String host = getHost();
                        int port = getPort();

                        try {
                            Hashtable<String, String> environment = new Hashtable<String, String>();
                            environment.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
                            environment.put("java.naming.provider.url", "dns:");

                            String[] result = new InitialDirContext(environment).getAttributes(getPacketProtocol().getSRVRecordPrefix() + "._tcp." + host, new String[] { "SRV" }).get("srv").get().toString().split(" ", 4);
                            host = result[3];
                            port = Integer.parseInt(result[2]);
                        } catch(Throwable t) {
                        }

                        bootstrap.remoteAddress(host, port);
                        ChannelFuture future = bootstrap.connect();
                        future.addListener(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {
                                complete.set(true);
                                if(!future.isSuccess() && future.cause() != null) {
                                    exceptionCaught(null, future.cause());
                                }
                            }
                        });
                    } catch(Throwable t) {
                        complete.set(true);
                        exceptionCaught(null, t);
                    }
                }
            }).start();

            if(wait) {
                while(!complete.get()) {
                    try {
                        Thread.sleep(5);
                    } catch(InterruptedException e) {
                        break;
                    }
                }
            }
        } catch(Throwable t) {
            exceptionCaught(null, t);
        }
    }

    @Override
    public void disconnect(String reason, Throwable cause, boolean wait) {
        super.disconnect(reason, cause, wait);
        if(this.group != null) {
            Future<?> future = this.group.shutdownGracefully();
            if(wait) {
                try {
                    future.await();
                } catch(InterruptedException e) {
                }
            }

            this.group = null;
        }
    }
}
