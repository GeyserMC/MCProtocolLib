package org.spacehq.packetlib;

/**
 * Listens for new sessions to connect.
 */
public interface ConnectionListener {
    /**
     * Gets the host the session is listening on.
     *
     * @return The listening host.
     */
    public String getHost();

    /**
     * Gets the port the session is listening on.
     *
     * @return The listening port.
     */
    public int getPort();

    /**
     * Returns true if the listener is listening.
     *
     * @return True if the listener is listening.
     */
    public boolean isListening();

    /**
     * Binds the listener to its host and port.
     */
    public void bind();

    /**
     * Closes the listener.
     */
    public void close();

    /**
     * Closes the listener.
     *
     * @param wait Whether to wait for the listener to finish closing.
     */
    public void close(boolean wait);
}
