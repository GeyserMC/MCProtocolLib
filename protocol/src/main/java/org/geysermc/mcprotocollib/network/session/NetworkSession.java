package org.geysermc.mcprotocollib.network.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
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
import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class NetworkSession extends SimpleChannelInboundHandler<Packet> implements Session {
    private static final Logger log = LoggerFactory.getLogger(NetworkSession.class);

    protected final SocketAddress remoteAddress;
    protected final MinecraftProtocol protocol;
    protected final Executor packetHandlerExecutor;

    private final Queue<Consumer<NetworkSession>> pendingActions = new ConcurrentLinkedQueue<>();
    private final Map<String, Object> flags = new HashMap<>();
    private final List<SessionListener> listeners = new CopyOnWriteArrayList<>();

    private Channel channel;
    protected boolean disconnected = false;
    @Nullable
    private volatile Component delayedDisconnect;

    public NetworkSession(
        @NonNull SocketAddress remoteAddress,
        @NonNull MinecraftProtocol protocol,
        @NonNull Executor packetHandlerExecutor
    ) {
        this.remoteAddress = remoteAddress;
        this.protocol = protocol;
        this.packetHandlerExecutor = packetHandlerExecutor;
    }

    @Override
    public SocketAddress getLocalAddress() {
        return this.channel != null ? this.channel.localAddress() : null;
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public MinecraftProtocol getPacketProtocol() {
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
    public <T> T getFlagSupplied(Flag<T> flag, Supplier<T> defSupplier) {
        Object value = this.flags.get(flag.key());
        if (value == null) {
            return defSupplier.get();
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
            this.disconnect(this.getGenericDisconnectMessage(t), t);
        }
    }

    @Override
    public void callPacketReceived(Packet packet) {
        try {
            for (SessionListener listener : this.listeners) {
                listener.packetReceived(this, packet);
            }
        } catch (Throwable t) {
            this.disconnect(this.getGenericDisconnectMessage(t), t);
        }
    }

    @Override
    public void callPacketSent(Packet packet) {
        try {
            for (SessionListener listener : this.listeners) {
                listener.packetSent(this, packet);
            }
        } catch (Throwable t) {
            this.disconnect(this.getGenericDisconnectMessage(t), t);
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
        return this.channel != null && this.channel.isOpen();
    }

    @Override
    public void send(@NonNull Packet packet, @Nullable Runnable onSent) {
        if (this.isConnected()) {
            this.flushQueue();
            this.sendPacket(packet, onSent);
        } else {
            this.pendingActions.add(session -> session.sendPacket(packet, onSent));
        }
    }

    private void sendPacket(@NonNull Packet packet, @Nullable Runnable onSent) {
        if (this.channel.eventLoop().inEventLoop()) {
            this.doSendPacket(packet, onSent);
        } else {
            this.channel.eventLoop().execute(() -> this.doSendPacket(packet, onSent));
        }
    }

    private void doSendPacket(@NonNull Packet packet, @Nullable Runnable onSent) {
        PacketSendingEvent sendingEvent = new PacketSendingEvent(this, packet);
        this.callEvent(sendingEvent);

        if (!sendingEvent.isCancelled()) {
            final Packet toSend = sendingEvent.getPacket();
            this.channel.writeAndFlush(toSend).addListener((ChannelFutureListener) future -> {
                if (onSent != null) {
                    onSent.run();
                }

                // See PacketSendListener#thenRun - runnable first, success check later
                if (!future.isSuccess()) {
                    // Mirrors Java client handling; uses a void promise when there's no runnable
                    if (onSent != null) {
                        channel.pipeline().fireExceptionCaught(future.cause());
                    }
                } else {
                    callPacketSent(toSend);
                }
            });
        }
    }

    @Override
    public void disconnect(@NonNull Component reason, @Nullable Throwable cause) {
        if (this.channel == null) {
            this.delayedDisconnect = reason;
        }

        if (this.disconnected) {
            return;
        }

        this.disconnected = true;

        if (this.isConnected()) {
            this.callEvent(new DisconnectingEvent(this, reason, cause));
            this.channel.flush().close().awaitUninterruptibly();
            this.callEvent(new DisconnectedEvent(NetworkSession.this, reason, cause));
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
    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public Executor getPacketHandlerExecutor() {
        return this.packetHandlerExecutor;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        this.channel = ctx.channel();

        Component delayedDisconnect = this.delayedDisconnect;
        if (delayedDisconnect != null) {
            this.disconnect(delayedDisconnect);
        } else {
            this.callEvent(new ConnectedEvent(this));
            this.flushQueue();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.disconnect(Component.translatable("disconnect.endOfStream"));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (this.channel.isOpen()) {
            Component message;
            if (cause instanceof TimeoutException) {
                message = Component.translatable("disconnect.timeout");
            } else {
                message = this.getGenericDisconnectMessage(cause);
            }

            this.disconnect(message, cause);
        }
    }

    private void flushQueue() {
        if (this.channel != null && this.channel.isOpen()) {
            synchronized (this.pendingActions) {
                Consumer<NetworkSession> consumer;
                while ((consumer = this.pendingActions.poll()) != null) {
                    consumer.accept(this);
                }
            }
        }
    }

    protected Component getGenericDisconnectMessage(Throwable cause) {
        return Component.translatable("disconnect.genericReason", Component.text("Internal Exception: " + cause));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {
        if (!this.channel.isOpen()) {
            return;
        }

        if (packet.shouldRunOnGameThread()) {
            packetHandlerExecutor.execute(() -> this.callPacketReceived(packet));
        } else {
            this.callPacketReceived(packet);
        }
    }
}
