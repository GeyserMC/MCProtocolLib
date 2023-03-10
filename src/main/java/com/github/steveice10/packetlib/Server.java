package com.github.steveice10.packetlib;

import com.github.steveice10.packetlib.event.server.ServerListener;
import com.github.steveice10.packetlib.packet.PacketProtocol;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Listens for new sessions to connect.
 */
public interface Server {
    /**
     * Gets the host the session is listening on.
     *
     * @return The listening host.
     */
    String getHost();

    /**
     * Gets the port the session is listening on.
     *
     * @return The listening port.
     */
    int getPort();

    /**
     * Gets the packet protocol of the server.
     *
     * @return The server's packet protocol.
     */
    Supplier<? extends PacketProtocol> getPacketProtocol();

    /**
     * Returns true if the listener is listening.
     *
     * @return True if the listener is listening.
     */
    boolean isListening();

    /**
     * Gets this server's set flags.
     *
     * @return This server's flags.
     */
    Map<String, Object> getGlobalFlags();

    /**
     * Checks whether this server has a flag set.
     *
     * @param key Key of the flag to check for.
     * @return Whether this server has a flag set.
     */
    boolean hasGlobalFlag(String key);

    /**
     * Gets the value of the given flag as an instance of the given type.
     *
     * @param <T> Type of the flag.
     * @param key Key of the flag.
     * @return Value of the flag.
     * @throws IllegalStateException If the flag's value isn't of the required type.
     */
    <T> T getGlobalFlag(String key);

    /**
     * Gets the value of the given flag as an instance of the given type.
     * If the flag is not set, the specified default value will be returned.
     *
     * @param <T> Type of the flag.
     * @param key Key of the flag.
     * @param def Default value of the flag.
     * @return Value of the flag.
     * @throws IllegalStateException If the flag's value isn't of the required type.
     */
    @SuppressWarnings("unchecked")
    <T> T getGlobalFlag(String key, T def);

    /**
     * Sets the value of a flag. The flag will be used in sessions if a session does
     * not contain a value for the flag.
     *
     * @param key   Key of the flag.
     * @param value Value to set the flag to.
     */
    void setGlobalFlag(String key, Object value);

    /**
     * Gets the listeners listening on this session.
     *
     * @return This server's listeners.
     */
    List<ServerListener> getListeners();

    /**
     * Adds a listener to this server.
     *
     * @param listener Listener to add.
     */
    void addListener(ServerListener listener);

    /**
     * Removes a listener from this server.
     *
     * @param listener Listener to remove.
     */
    void removeListener(ServerListener listener);

    /**
     * Gets all sessions belonging to this server.
     *
     * @return Sessions belonging to this server.
     */
    List<Session> getSessions();

    /**
     * Binds the listener to its host and port.
     */
    AbstractServer bind();

    /**
     * Binds the listener to its host and port.
     *
     * @param wait Whether to wait for the listener to finish binding.
     */
    AbstractServer bind(boolean wait);

    /**
     * Binds the listener to its host and port.
     *
     * @param wait     Whether to wait for the listener to finish binding.
     * @param callback Callback to call when the listener has finished binding.
     */
    AbstractServer bind(boolean wait, Runnable callback);

    /**
     * Closes the listener.
     */
    void close();

    /**
     * Closes the listener.
     *
     * @param wait Whether to wait for the listener to finish closing.
     */
    void close(boolean wait);

    /**
     * Closes the listener.
     *
     * @param wait     Whether to wait for the listener to finish closing.
     * @param callback Callback to call when the listener has finished closing.
     */
    void close(boolean wait, Runnable callback);
}
