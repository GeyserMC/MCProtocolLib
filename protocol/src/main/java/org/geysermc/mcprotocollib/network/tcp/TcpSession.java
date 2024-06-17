package org.geysermc.mcprotocollib.network.tcp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.network.Flag;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.crypt.PacketEncryption;
import org.geysermc.mcprotocollib.network.event.session.ConnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.DisconnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.DisconnectingEvent;
import org.geysermc.mcprotocollib.network.event.session.PacketSendingEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionListener;
import org.geysermc.mcprotocollib.network.packet.FakeFlushPacket;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.network.packet.PacketProtocol;

import java.net.SocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class TcpSession extends SimpleChannelInboundHandler<Packet> implements Session {
    /**
     * Controls whether non-priority packets are handled in a separate event loop
     */
    public static boolean USE_EVENT_LOOP_FOR_PACKETS = true;
    private static EventLoopGroup PACKET_EVENT_LOOP;
    private static final int SHUTDOWN_QUIET_PERIOD_MS = 100;
    private static final int SHUTDOWN_TIMEOUT_MS = 500;

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
        this.connect(wait, false);
    }

    @Override
    public void connect(boolean wait, boolean transferring) {
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
    public boolean hasFlag(Flag<?> flag) {
        return this.flags.containsKey(flag.key());
    }

    @Override
    public <T> T getFlag(Flag<T> flag) {
        return this.getFlag(flag, null);
    }

    @Override
    public <T> T getFlag(Flag<T> flag, T def) {
        Object value = this.flags.get(flag.key());
        if (value == null) {
            return def;
        }

        try {
            return flag.cast(value);
        } catch (ClassCastException e) {
            throw new IllegalStateException("Tried to get flag \"" + flag.key() + "\" as the wrong type. Actual type: " + value.getClass().getName());
        }
    }

    @Override
    public <T> void setFlag(Flag<T> flag, T value) {
        this.flags.put(flag.key(), value);
    }

    @Override
    public void setFlags(Map<String, Object> flags) {
        this.flags.putAll(flags);
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
        } catch (Throwable t) {
            exceptionCaught(null, t);
        }
    }

    @Override
    public void callPacketReceived(Packet packet) {
        try {
            for (SessionListener listener : this.listeners) {
                listener.packetReceived(this, packet);
            }
        } catch (Throwable t) {
            exceptionCaught(null, t);
        }
    }

    @Override
    public void callPacketSent(Packet packet) {
        try {
            for (SessionListener listener : this.listeners) {
                listener.packetSent(this, packet);
            }
        } catch (Throwable t) {
            exceptionCaught(null, t);
        }
    }

    @Override
    public int getCompressionThreshold() {
        return this.compressionThreshold;
    }

    @Override
    public void setCompressionThreshold(int threshold, boolean validateDecompression) {
        this.compressionThreshold = threshold;
        if (this.channel != null) {
            if (this.compressionThreshold >= 0) {
                if (this.channel.pipeline().get("compression") == null) {
                    this.channel.pipeline().addBefore("codec", "compression", new TcpPacketCompression(this, validateDecompression));
                }
            } else if (this.channel.pipeline().get("compression") != null) {
                this.channel.pipeline().remove("compression");
            }
        }
    }

    @Override
    public void enableEncryption(PacketEncryption encryption) {
        if (channel == null) {
            throw new IllegalStateException("Connect the client before initializing encryption!");
        }
        channel.pipeline().addBefore("sizer", "encryption", new TcpPacketEncryptor(encryption));
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
    public void send(Packet packet, Runnable onSent) {
        if (this.channel == null) {
            return;
        }

        PacketSendingEvent sendingEvent = new PacketSendingEvent(this, packet);
        this.callEvent(sendingEvent);

        if (!sendingEvent.isCancelled()) {
            final Packet toSend = sendingEvent.getPacket();
            this.channel.writeAndFlush(toSend).addListener(future -> {
                if (future.isSuccess()) {
                    if (onSent != null) {
                        onSent.run();
                    }

                    callPacketSent(toSend);
                } else {
                    exceptionCaught(null, future.cause());
                }
            });
        }
    }

    @Override
    public void disconnect(@NonNull Component reason) {
        this.disconnect(reason, null);
    }

    @Override
    public void disconnect(@NonNull Component reason, Throwable cause) {
        if (this.disconnected) {
            return;
        }

        this.disconnected = true;

        if (this.channel != null && this.channel.isOpen()) {
            this.callEvent(new DisconnectingEvent(this, reason, cause));
            this.channel.flush().close().addListener(future ->
                callEvent(new DisconnectedEvent(TcpSession.this, reason, cause)));
        } else {
            this.callEvent(new DisconnectedEvent(this, reason, cause));
        }
    }

    @Override
    public void setAutoRead(boolean autoRead) {
        if (this.channel != null) {
            this.channel.config().setAutoRead(autoRead);
        }
    }

    @Override
    public void flushSync() {
        if (this.channel != null) {
            this.channel.writeAndFlush(FakeFlushPacket.INSTANCE).syncUninterruptibly();
        }
    }

    private @Nullable EventLoop createEventLoop() {
        if (!USE_EVENT_LOOP_FOR_PACKETS) {
            return null;
        }

        if (PACKET_EVENT_LOOP == null) {
            // See TcpClientSession.newThreadFactory() for details on
            // daemon threads and their interaction with the runtime.
            PACKET_EVENT_LOOP = new DefaultEventLoopGroup(new DefaultThreadFactory(this.getClass(), true));
            Runtime.getRuntime().addShutdownHook(new Thread(
                () -> PACKET_EVENT_LOOP.shutdownGracefully(SHUTDOWN_QUIET_PERIOD_MS, SHUTDOWN_TIMEOUT_MS, TimeUnit.MILLISECONDS)));
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
            this.disconnect(Component.translatable("disconnect.endOfStream"));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Component message;
        if (cause instanceof TimeoutException) {
            message = Component.translatable("disconnect.timeout");
        } else {
            message = Component.translatable("disconnect.genericReason", Component.text("Internal Exception: " + cause));
        }

        this.disconnect(message, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {
        if (packet.isTerminal()) {
            // Next packets are in a different protocol state, so we must
            // disable auto-read to prevent reading wrong packets.
            setAutoRead(false);
        }

        this.callPacketReceived(packet);
    }
}
