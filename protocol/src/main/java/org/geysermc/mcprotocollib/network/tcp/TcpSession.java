package org.geysermc.mcprotocollib.network.tcp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.network.Flag;
import org.geysermc.mcprotocollib.network.NetworkConstants;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.compression.CompressionConfig;
import org.geysermc.mcprotocollib.network.crypt.EncryptionConfig;
import org.geysermc.mcprotocollib.network.event.session.ConnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.DisconnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.DisconnectingEvent;
import org.geysermc.mcprotocollib.network.event.session.PacketSendingEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionListener;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.network.packet.PacketProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class TcpSession extends SimpleChannelInboundHandler<Packet> implements Session {
    private static final Logger log = LoggerFactory.getLogger(TcpSession.class);

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
    public void setCompression(@Nullable CompressionConfig compressionConfig) {
        if (this.channel == null) {
            throw new IllegalStateException("You need to connect to set the compression!");
        }

        log.debug("Setting compression for session {}", this);
        channel.attr(NetworkConstants.COMPRESSION_ATTRIBUTE_KEY).set(compressionConfig);
    }

    @Override
    public void setEncryption(@Nullable EncryptionConfig encryptionConfig) {
        if (channel == null) {
            throw new IllegalStateException("You need to connect to set the encryption!");
        }

        log.debug("Setting encryption for session {}", this);
        channel.attr(NetworkConstants.ENCRYPTION_ATTRIBUTE_KEY).set(encryptionConfig);
    }

    @Override
    public boolean isConnected() {
        return this.channel != null && this.channel.isOpen() && !this.disconnected;
    }

    @Override
    public void send(Packet packet) {
        if (this.channel == null) {
            return;
        }

        PacketSendingEvent sendingEvent = new PacketSendingEvent(this, packet);
        this.callEvent(sendingEvent);

        if (!sendingEvent.isCancelled()) {
            final Packet toSend = sendingEvent.getPacket();
            this.channel.writeAndFlush(toSend).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    callPacketSent(toSend);
                } else {
                    exceptionCaught(null, future.cause());
                }
            });
        }
    }

    @Override
    public void disconnect(@NonNull Component reason, @Nullable Throwable cause) {
        if (this.disconnected) {
            return;
        }

        this.disconnected = true;

        if (this.channel != null && this.channel.isOpen()) {
            this.callEvent(new DisconnectingEvent(this, reason, cause));
            this.channel.flush().close().addListener((ChannelFutureListener) future -> callEvent(new DisconnectedEvent(TcpSession.this, reason, cause)));
        } else {
            this.callEvent(new DisconnectedEvent(this, reason, cause));
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
        if (!packet.isPriority() && eventLoop != null) {
            eventLoop.execute(() -> this.callPacketReceived(packet));
        } else {
            this.callPacketReceived(packet);
        }
    }
}
