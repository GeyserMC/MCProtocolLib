package org.spacehq.packetlib;

import org.spacehq.packetlib.event.session.SessionEvent;
import org.spacehq.packetlib.event.session.SessionListener;
import org.spacehq.packetlib.packet.Packet;
import org.spacehq.packetlib.packet.PacketProtocol;

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
    public void connect();

    /**
     * Connects this session to its host and port.
     *
     * @param wait Whether to wait for the connection to be established before returning.
     */
    public void connect(boolean wait);

    /**
     * Gets the host the session is connected to.
     *
     * @return The connected host.
     */
    public String getHost();

    /**
     * Gets the port the session is connected to.
     *
     * @return The connected port.
     */
    public int getPort();

    /**
     * Gets the local address of the session.
     *
     * @return The local address, or null if the session is not connected.
     */
    public SocketAddress getLocalAddress();

    /**
     * Gets the remote address of the session.
     *
     * @return The remote address, or null if the session is not connected.
     */
    public SocketAddress getRemoteAddress();

    /**
     * Gets the packet protocol of the session.
     *
     * @return The session's packet protocol.
     */
    public PacketProtocol getPacketProtocol();

    /**
     * Gets this session's set flags. If this session belongs to a server, the server's
     * flags will be included in the results.
     *
     * @return This session's flags.
     */
    public Map<String, Object> getFlags();

    /**
     * Checks whether this session has a flag set. If this session belongs to a server,
     * the server's flags will also be checked.
     *
     * @param key Key of the flag to check for.
     * @return Whether this session has a flag set.
     */
    public boolean hasFlag(String key);

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
    public <T> T getFlag(String key);

    /**
     * Sets the value of a flag. This does not change a server's flags if this session
     * belongs to a server.
     *
     * @param key   Key of the flag.
     * @param value Value to set the flag to.
     */
    public void setFlag(String key, Object value);

    /**
     * Gets the listeners listening on this session.
     *
     * @return This session's listeners.
     */
    public List<SessionListener> getListeners();

    /**
     * Adds a listener to this session.
     *
     * @param listener Listener to add.
     */
    public void addListener(SessionListener listener);

    /**
     * Removes a listener from this session.
     *
     * @param listener Listener to remove.
     */
    public void removeListener(SessionListener listener);

    /**
     * Calls an event on the listeners of this session.
     *
     * @param event Event to call.
     */
    public void callEvent(SessionEvent event);

    /**
     * Gets the compression packet length threshold for this session (-1 = disabled).
     *
     * @return This session's compression threshold.
     */
    public int getCompressionThreshold();

    /**
     * Sets the compression packet length threshold for this session (-1 = disabled).
     *
     * @param threshold The new compression threshold.
     */
    public void setCompressionThreshold(int threshold);

    /**
     * Gets the connect timeout for this session in seconds.
     *
     * @return The session's connect timeout.
     */
    public int getConnectTimeout();

    /**
     * Sets the connect timeout for this session in seconds.
     *
     * @param timeout Connect timeout to set.
     */
    public void setConnectTimeout(int timeout);

    /**
     * Gets the read timeout for this session in seconds.
     *
     * @return The session's read timeout.
     */
    public int getReadTimeout();

    /**
     * Sets the read timeout for this session in seconds.
     *
     * @param timeout Read timeout to set.
     */
    public void setReadTimeout(int timeout);

    /**
     * Gets the write timeout for this session in seconds.
     *
     * @return The session's write timeout.
     */
    public int getWriteTimeout();

    /**
     * Sets the write timeout for this session in seconds.
     *
     * @param timeout Write timeout to set.
     */
    public void setWriteTimeout(int timeout);

    /**
     * Returns true if the session is connected.
     *
     * @return True if the session is connected.
     */
    public boolean isConnected();

    /**
     * Sends a packet.
     *
     * @param packet Packet to send.
     */
    public void send(Packet packet);

    /**
     * Disconnects the session.
     *
     * @param reason Reason for disconnecting.
     */
    public void disconnect(String reason);

    /**
     * Disconnects the session.
     *
     * @param reason Reason for disconnecting.
     * @param wait   Whether to wait for the session to be disconnected.
     */
    public void disconnect(String reason, boolean wait);

    /**
     * Disconnects the session.
     *
     * @param reason Reason for disconnecting.
     * @param cause  Throwable responsible for disconnecting.
     */
    public void disconnect(String reason, Throwable cause);

    /**
     * Disconnects the session.
     *
     * @param reason Reason for disconnecting.
     * @param cause  Throwable responsible for disconnecting.
     * @param wait   Whether to wait for the session to be disconnected.
     */
    public void disconnect(String reason, Throwable cause, boolean wait);
}
