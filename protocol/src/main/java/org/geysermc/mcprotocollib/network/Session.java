package org.geysermc.mcprotocollib.network;

import org.geysermc.mcprotocollib.network.codec.PacketCodecHelper;
import org.geysermc.mcprotocollib.network.crypt.PacketEncryption;
import org.geysermc.mcprotocollib.network.event.session.SessionEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionListener;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.network.packet.PacketProtocol;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;

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
     * @param key Key of the flag to check for.
     * @return Whether this session has a flag set.
     */
    boolean hasFlag(String key);

    /**
     * Gets the value of the given flag as an instance of the given type. If this
     * session belongs to a server, the server's flags will be checked for the flag
     * as well.
     *
     * @param <T> Type of the flag.
     * @param key Key of the flag.
     * @return Value of the flag.
     * @throws IllegalStateException If the flag's value isn't of the required type.
     */
    <T> T getFlag(String key);

    /**
     * Gets the value of the given flag as an instance of the given type. If this
     * session belongs to a server, the server's flags will be checked for the flag
     * as well. If the flag is not set, the specified default value will be returned.
     *
     * @param <T> Type of the flag.
     * @param key Key of the flag.
     * @param def Default value of the flag.
     * @return Value of the flag.
     * @throws IllegalStateException If the flag's value isn't of the required type.
     */
    <T> T getFlag(String key, T def);

    /**
     * Sets the value of a flag. This does not change a server's flags if this session
     * belongs to a server.
     *
     * @param key   Key of the flag.
     * @param value Value to set the flag to.
     */
    void setFlag(String key, Object value);

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
     * Gets the compression packet length threshold for this session (-1 = disabled).
     *
     * @return This session's compression threshold.
     */
    int getCompressionThreshold();

    /**
     * Sets the compression packet length threshold for this session (-1 = disabled).
     *
     * @param threshold The new compression threshold.
     * @param validateDecompression whether to validate that the decompression fits within size checks.
     */
    void setCompressionThreshold(int threshold, boolean validateDecompression);

    /**
     * Enables encryption for this session.
     *
     * @param encryption the encryption to encrypt with
     */
    void enableEncryption(PacketEncryption encryption);

    /**
     * Gets the connect timeout for this session in seconds.
     *
     * @return The session's connect timeout.
     */
    int getConnectTimeout();

    /**
     * Sets the connect timeout for this session in seconds.
     *
     * @param timeout Connect timeout to set.
     */
    void setConnectTimeout(int timeout);

    /**
     * Gets the read timeout for this session in seconds.
     *
     * @return The session's read timeout.
     */
    int getReadTimeout();

    /**
     * Sets the read timeout for this session in seconds.
     *
     * @param timeout Read timeout to set.
     */
    void setReadTimeout(int timeout);

    /**
     * Gets the write timeout for this session in seconds.
     *
     * @return The session's write timeout.
     */
    int getWriteTimeout();

    /**
     * Sets the write timeout for this session in seconds.
     *
     * @param timeout Write timeout to set.
     */
    void setWriteTimeout(int timeout);

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
     *
     * @param reason Reason for disconnecting.
     */
    void disconnect(@Nullable String reason);

    /**
     * Disconnects the session.
     *
     * @param reason Reason for disconnecting.
     * @param cause  Throwable responsible for disconnecting.
     */
    void disconnect(@Nullable String reason, Throwable cause);

    /**
     * Disconnects the session.
     *
     * @param reason Reason for disconnecting.
     */
    void disconnect(@Nullable Component reason);

    /**
     * Disconnects the session.
     *
     * @param reason Reason for disconnecting.
     * @param cause  Throwable responsible for disconnecting.
     */
    void disconnect(@Nullable Component reason, Throwable cause);
}
