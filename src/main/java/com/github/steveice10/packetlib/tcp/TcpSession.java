package com.github.steveice10.packetlib.tcp;

import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectingEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.PacketSendingEvent;
import com.github.steveice10.packetlib.event.session.PacketSentEvent;
import com.github.steveice10.packetlib.event.session.SessionEvent;
import com.github.steveice10.packetlib.event.session.SessionListener;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.packet.PacketProtocol;
import io.netty.channel.*;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutException;
import io.netty.handler.timeout.WriteTimeoutHandler;

import javax.annotation.Nullable;
import java.net.ConnectException;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class TcpSession extends SimpleChannelInboundHandler<Packet> implements Session {
    /**
     * Controls whether non-priority packets are handled in a separate event loop
     */
    public static boolean USE_EVENT_LOOP_FOR_PACKETS = true;
    private static EventLoopGroup PACKET_EVENT_LOOP;

    protected String host;
    protected int port;
    private final PacketProtocol protocol;
    private final EventLoop eventLoop = createEventLoop();

    private int compressionThreshold = -1;
    private int connectTimeout = 30;
    private int readTimeout = 30;
    private int writeTimeout = 0;

    private final Map<String, Object> flags = new HashMap<>();
    private final List<SessionListener> listeners = new CopyOnWriteArrayList<>();

    private Channel channel;
    protected boolean disconnected = false;

    public TcpSession(String host, int port, PacketProtocol protocol) {
        this.host = host;
        this.port = port;
        this.protocol = protocol;
    }

    @Override
    public void connect() {
        this.connect(true);
    }

    @Override
    public void connect(boolean wait) {
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
    public SocketAddress getLocalAddress() {
        return this.channel != null ? this.channel.localAddress() : null;
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return this.channel != null ? this.channel.remoteAddress() : null;
    }

    @Override
    public PacketProtocol getPacketProtocol() {
        return this.protocol;
    }

    @Override
    public Map<String, Object> getFlags() {
        return Collections.unmodifiableMap(this.flags);
    }

    @Override
    public boolean hasFlag(String key) {
        return this.flags.containsKey(key);
    }

    @Override
    public <T> T getFlag(String key) {
        return this.getFlag(key, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getFlag(String key, T def) {
        Object value = this.flags.get(key);
        if (value == null) {
            return def;
        }

        try {
            return (T) value;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Tried to get flag \"" + key + "\" as the wrong type. Actual type: " + value.getClass().getName());
        }
    }

    @Override
    public void setFlag(String key, Object value) {
        this.flags.put(key, value);
    }

    @Override
    public List<SessionListener> getListeners() {
        return Collections.unmodifiableList(this.listeners);
    }

    @Override
    public void addListener(SessionListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(SessionListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void callEvent(SessionEvent event) {
        try {
            for (SessionListener listener : this.listeners) {
                event.call(listener);
            }
        } catch(Throwable t) {
            exceptionCaught(null, t);
        }
    }

    @Override
    public int getCompressionThreshold() {
        return this.compressionThreshold;
    }

    @Override
    public void setCompressionThreshold(int threshold) {
        this.compressionThreshold = threshold;
        if (this.channel != null) {
            if (this.compressionThreshold >= 0) {
                if (this.channel.pipeline().get("compression") == null) {
                    this.channel.pipeline().addBefore("codec", "compression", new TcpPacketCompression(this));
                }
            } else if (this.channel.pipeline().get("compression") != null) {
                this.channel.pipeline().remove("compression");
            }
        }
    }

    @Override
    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    @Override
    public void setConnectTimeout(int timeout) {
        this.connectTimeout = timeout;
    }

    @Override
    public int getReadTimeout() {
        return this.readTimeout;
    }

    @Override
    public void setReadTimeout(int timeout) {
        this.readTimeout = timeout;
        this.refreshReadTimeoutHandler();
    }

    @Override
    public int getWriteTimeout() {
        return this.writeTimeout;
    }

    @Override
    public void setWriteTimeout(int timeout) {
        this.writeTimeout = timeout;
        this.refreshWriteTimeoutHandler();
    }

    @Override
    public boolean isConnected() {
        return this.channel != null && this.channel.isOpen() && !this.disconnected;
    }

    @Override
    public void send(Packet packet) {
        if(this.channel == null) {
            return;
        }

        PacketSendingEvent sendingEvent = new PacketSendingEvent(this, packet);
        this.callEvent(sendingEvent);

        if (!sendingEvent.isCancelled()) {
            final Packet toSend = sendingEvent.getPacket();
            this.channel.writeAndFlush(toSend).addListener((ChannelFutureListener) future -> {
                if(future.isSuccess()) {
                    callEvent(new PacketSentEvent(TcpSession.this, toSend));
                } else {
                    exceptionCaught(null, future.cause());
                }
            });
        }
    }

    @Override
    public void disconnect(String reason) {
        this.disconnect(reason, null);
    }

    @Override
    public void disconnect(final String reason, final Throwable cause) {
        if (this.disconnected) {
            return;
        }

        this.disconnected = true;

        if (this.channel != null && this.channel.isOpen()) {
            this.callEvent(new DisconnectingEvent(this, reason, cause));
            this.channel.flush().close().addListener((ChannelFutureListener) future ->
                    callEvent(new DisconnectedEvent(TcpSession.this,
                            reason != null ? reason : "Connection closed.", cause)));
        } else {
            this.callEvent(new DisconnectedEvent(this, reason != null ? reason : "Connection closed.", cause));
        }
    }

    private @Nullable EventLoop createEventLoop() {
        if (!USE_EVENT_LOOP_FOR_PACKETS) {
            return null;
        }

        if (PACKET_EVENT_LOOP == null) {
            PACKET_EVENT_LOOP = new DefaultEventLoopGroup();
        }
        return PACKET_EVENT_LOOP.next();
    }

    public Channel getChannel() {
        return this.channel;
    }

    protected void refreshReadTimeoutHandler() {
        this.refreshReadTimeoutHandler(this.channel);
    }

    protected void refreshReadTimeoutHandler(Channel channel) {
        if (channel != null) {
            if (this.readTimeout <= 0) {
                if (channel.pipeline().get("readTimeout") != null) {
                    channel.pipeline().remove("readTimeout");
                }
            } else {
                if (channel.pipeline().get("readTimeout") == null) {
                    channel.pipeline().addFirst("readTimeout", new ReadTimeoutHandler(this.readTimeout));
                } else {
                    channel.pipeline().replace("readTimeout", "readTimeout", new ReadTimeoutHandler(this.readTimeout));
                }
            }
        }
    }

    protected void refreshWriteTimeoutHandler() {
        this.refreshWriteTimeoutHandler(this.channel);
    }

    protected void refreshWriteTimeoutHandler(Channel channel) {
        if (channel != null) {
            if (this.writeTimeout <= 0) {
                if (channel.pipeline().get("writeTimeout") != null) {
                    channel.pipeline().remove("writeTimeout");
                }
            } else {
                if (channel.pipeline().get("writeTimeout") == null) {
                    channel.pipeline().addFirst("writeTimeout", new WriteTimeoutHandler(this.writeTimeout));
                } else {
                    channel.pipeline().replace("writeTimeout", "writeTimeout", new WriteTimeoutHandler(this.writeTimeout));
                }
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (this.disconnected || this.channel != null) {
            ctx.channel().close();
            return;
        }

        this.channel = ctx.channel();

        this.callEvent(new ConnectedEvent(this));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel() == this.channel) {
            this.disconnect("Connection closed.");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String message;
        if (cause instanceof ConnectTimeoutException || (cause instanceof ConnectException && cause.getMessage().contains("connection timed out"))) {
            message = "Connection timed out.";
        } else if (cause instanceof ReadTimeoutException) {
            message = "Read timed out.";
        } else if (cause instanceof WriteTimeoutException) {
            message = "Write timed out.";
        } else {
            message = cause.toString();
        }

        this.disconnect(message, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {
        if (!packet.isPriority() && eventLoop != null) {
            eventLoop.execute(() -> this.callEvent(new PacketReceivedEvent(this, packet)));
        } else {
            this.callEvent(new PacketReceivedEvent(this, packet));
        }
    }
}
