package org.geysermc.mcprotocollib.network;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.network.codec.PacketCodecHelper;
import org.geysermc.mcprotocollib.network.compression.CompressionConfig;
import org.geysermc.mcprotocollib.network.crypt.EncryptionConfig;
import org.geysermc.mcprotocollib.network.event.session.SessionEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionListener;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.network.packet.PacketProtocol;

import java.net.SocketAddress;
import java.util.List;
import java.util.Map;

/**
 * A network session.
 */
public interface Session {

    /**
     * Connects this session to its host and port.
     */
    void connect();

    /**
     * Connects this session to its host and port.
     *
     * @param wait Whether to wait for the connection to be established before returning.
     */
    void connect(boolean wait);

    /**
     * Connects this session to its host and port.
     *
     * @param wait Whether to wait for the connection to be established before returning.
     * @param transferring Whether the session is a client being transferred.
     */
    void connect(boolean wait, boolean transferring);

    /**
     * Gets the host the session is connected to.
     *
     * @return The connected host.
     */
    String getHost();

    /**
     * Gets the port the session is connected to.
     *
     * @return The connected port.
     */
    int getPort();

    /**
     * Gets the local address of the session.
     *
     * @return The local address, or null if the session is not connected.
     */
    SocketAddress getLocalAddress();

    /**
     * Gets the remote address of the session.
     *
     * @return The remote address, or null if the session is not connected.
     */
    SocketAddress getRemoteAddress();

    /**
     * Gets the packet protocol of the session.
     *
     * @return The session's packet protocol.
     */
    PacketProtocol getPacketProtocol();

    /**
     * Gets the session's {@link PacketCodecHelper}.
     *
     * @return The session's packet codec helper.
     */
    PacketCodecHelper getCodecHelper();

    /**
     * Gets this session's set flags. If this session belongs to a server, the server's
     * flags will be included in the results.
     *
     * @return This session's flags.
     */
    Map<String, Object> getFlags();

    /**
     * Checks whether this session has a flag set. If this session belongs to a server,
     * the server's flags will also be checked.
     *
     * @param flag Flag to check for.
     * @return Whether this session has a flag set.
     */
    boolean hasFlag(Flag<?> flag);

    /**
     * Gets the value of the given flag as an instance of the given type. If this
     * session belongs to a server, the server's flags will be checked for the flag
     * as well.
     *
     * @param <T> Type of the flag.
     * @param flag Flag to check for.
     * @return Value of the flag.
     * @throws IllegalStateException If the flag's value isn't of the required type.
     */
    <T> T getFlag(Flag<T> flag);

    /**
     * Gets the value of the given flag as an instance of the given type. If this
     * session belongs to a server, the server's flags will be checked for the flag
     * as well. If the flag is not set, the specified default value will be returned.
     *
     * @param <T> Type of the flag.
     * @param flag Flag to check for.
     * @param def Default value of the flag.
     * @return Value of the flag.
     * @throws IllegalStateException If the flag's value isn't of the required type.
     */
    <T> T getFlag(Flag<T> flag, T def);

    /**
     * Sets the value of a flag. This does not change a server's flags if this session
     * belongs to a server.
     *
     * @param <T> Type of the flag.
     * @param flag Flag to check for.
     * @param value Value to set the flag to.
     */
    <T> void setFlag(Flag<T> flag, T value);

    /**
     * Sets the values for a collection of flags.
     *
     * @param flags Collection of flags
     */
    void setFlags(Map<String, Object> flags);

    /**
     * Gets the listeners listening on this session.
     *
     * @return This session's listeners.
     */
    List<SessionListener> getListeners();

    /**
     * Adds a listener to this session.
     *
     * @param listener Listener to add.
     */
    void addListener(SessionListener listener);

    /**
     * Removes a listener from this session.
     *
     * @param listener Listener to remove.
     */
    void removeListener(SessionListener listener);

    /**
     * Calls an event on the listeners of this session.
     *
     * @param event Event to call.
     */
    void callEvent(SessionEvent event);

    /**
     * Notifies all listeners that a packet was just received.
     *
     * @param packet Packet to notify.
     */
    void callPacketReceived(Packet packet);

    /**
     * Notifies all listeners that a packet was just sent.
     *
     * @param packet Packet to notify.
     */
    void callPacketSent(Packet packet);

    /**
     * Sets the compression config for this session.
     *
     * @param compressionConfig the compression to compress with,
     *                          or null to disable compression
     */
    void setCompression(CompressionConfig compressionConfig);

    /**
     * Sets encryption for this session.
     *
     * @param encryptionConfig the encryption to encrypt with
     *                         or null to disable encryption
     *
     */
    void setEncryption(EncryptionConfig encryptionConfig);

                       /**
     * Returns true if the session is connected.
     *
     * @return True if the session is connected.
     */
    boolean isConnected();

    /**
     * Sends a packet.
     *
     * @param packet Packet to send.
     */
    void send(Packet packet);

    /**
     * Disconnects the session.
     * This method just wraps the reason into a {@link Component}.
     * It is recommended to use Components instead as they provide more flexibility.
     *
     * @param reason Reason for disconnecting.
     * @see #disconnect(String, Throwable)
     */
    default void disconnect(@NonNull String reason) {
        this.disconnect(reason, null);
    }

    /**
     * Disconnects the session.
     * This method just wraps the reason into a {@link Component}.
     * It is recommended to use Components instead as they provide more flexibility.
     *
     * @param reason Reason for disconnecting.
     * @param cause Throwable responsible for disconnecting.
     * @see #disconnect(Component, Throwable)
     */
    default void disconnect(@NonNull String reason, @Nullable Throwable cause) {
        this.disconnect(Component.text(reason), cause);
    }

    /**
     * Disconnects the session.
     *
     * @param reason Reason for disconnecting.
     */
    default void disconnect(@NonNull Component reason) {
        this.disconnect(reason, null);
    }

    /**
     * Disconnects the session.
     *
     * @param reason Reason for disconnecting.
     * @param cause Throwable responsible for disconnecting.
     */
    void disconnect(@NonNull Component reason, @Nullable Throwable cause);
}
