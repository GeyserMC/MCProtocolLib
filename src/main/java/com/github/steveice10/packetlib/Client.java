package com.github.steveice10.packetlib;

import com.github.steveice10.packetlib.packet.PacketProtocol;
import javax.annotation.Nullable;
import java.net.InetSocketAddress;

/**
 * A client that may connect to a server.
 */
public class Client {
    private String host;
    private int port;
    private String bindAddress;
    private int bindPort;
    private PacketProtocol protocol;
    private Session session;

    public Client(String host, int port, String bindAddress, int bindPort, PacketProtocol protocol, SessionFactory factory) {
        this.host = host;
        this.port = port;
        this.bindAddress = bindAddress;
        this.bindPort = bindPort;
        this.protocol = protocol;
        this.session = factory.createClientSession(this);
    }
    
    public Client(String host, int port, PacketProtocol protocol, SessionFactory factory) {
        this(host, port, "0.0.0.0", 0, protocol, factory);
    }

    /**
     * Gets the host the client is connecting to.
     *
     * @return The host the client is connecting to.
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Gets the port the client is connecting to.
     *
     * @return The port the client is connecting to.
     */
    public int getPort() {
        return this.port;
    }
    
    /**
     * Gets the the local address the client is connected from/will be binding to.
     *
     * @return Client's local IP address, or null if default and not connected.
     */
    @Nullable
    public String getBindAddress() {
        final Session session = this.getSession();
        return session.isConnected()
            ? ((InetSocketAddress) session.getLocalAddress()).getAddress().getHostAddress()
            : this.bindAddress;
    }
    
    /**
     * Gets the the local port the client is connected from/will be binding to.
     *
     * @return Client's local port, or 0 if default and not connected.
     */
    public int getBindPort() {
        final Session session = this.getSession();
        return session.isConnected()
            ? ((InetSocketAddress) session.getLocalAddress()).getPort()
            : this.bindPort;
    }
    
    /**
     * Gets the packet protocol of the client.
     *
     * @return The client's packet protocol.
     */
    public PacketProtocol getPacketProtocol() {
        return this.protocol;
    }

    /**
     * Gets the session of the client.
     *
     * @return The client's session.
     */
    public Session getSession() {
        return this.session;
    }
}
