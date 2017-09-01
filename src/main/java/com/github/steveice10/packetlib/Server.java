package com.github.steveice10.packetlib;

import com.github.steveice10.packetlib.event.server.ServerBoundEvent;
import com.github.steveice10.packetlib.event.server.ServerClosedEvent;
import com.github.steveice10.packetlib.event.server.ServerClosingEvent;
import com.github.steveice10.packetlib.event.server.ServerEvent;
import com.github.steveice10.packetlib.event.server.ServerListener;
import com.github.steveice10.packetlib.event.server.SessionAddedEvent;
import com.github.steveice10.packetlib.event.server.SessionRemovedEvent;
import com.github.steveice10.packetlib.packet.PacketProtocol;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A server that listens for connections.
 */
public class Server {
    private String host;
    private int port;
    private Class<? extends PacketProtocol> protocol;
    private SessionFactory factory;
    private ConnectionListener listener;
    private List<Session> sessions = new ArrayList<Session>();

    private Map<String, Object> flags = new HashMap<String, Object>();
    private List<ServerListener> listeners = new ArrayList<ServerListener>();

    public Server(String host, int port, Class<? extends PacketProtocol> protocol, SessionFactory factory) {
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.factory = factory;
    }

    /**
     * Binds and initializes the server.
     *
     * @return The server after being bound.
     */
    public Server bind() {
        return this.bind(true);
    }

    /**
     * Binds and initializes the server.
     *
     * @param wait Whether to wait for the server to finish binding.
     * @return The server after being bound.
     */
    public Server bind(boolean wait) {
        this.listener = this.factory.createServerListener(this);
        this.listener.bind(wait, new Runnable() {
            @Override
            public void run() {
                callEvent(new ServerBoundEvent(Server.this));
            }
        });

        return this;
    }

    /**
     * Gets the host this server is bound to.
     *
     * @return The server's host.
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Gets the port this server is bound to.
     *
     * @return The server's port.
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Gets the packet protocol of the server.
     *
     * @return The server's packet protocol.
     */
    public Class<? extends PacketProtocol> getPacketProtocol() {
        return this.protocol;
    }

    /**
     * Creates a new packet protocol instance from this server's protocol class.
     *
     * @return The created protocol instance.
     * @throws IllegalStateException If the protocol does not have a no-params constructor or cannot be instantiated.
     */
    public PacketProtocol createPacketProtocol() {
        try {
            Constructor<? extends PacketProtocol> constructor = this.protocol.getDeclaredConstructor();
            if(!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }

            return constructor.newInstance();
        } catch(NoSuchMethodError e) {
            throw new IllegalStateException("PacketProtocol \"" + this.protocol.getName() + "\" does not have a no-params constructor for instantiation.");
        } catch(Exception e) {
            throw new IllegalStateException("Failed to instantiate PacketProtocol " + this.protocol.getName() + ".", e);
        }
    }

    /**
     * Gets this server's set flags.
     *
     * @return This server's flags.
     */
    public Map<String, Object> getGlobalFlags() {
        return new HashMap<String, Object>(this.flags);
    }

    /**
     * Checks whether this server has a flag set.
     *
     * @param key Key of the flag to check for.
     * @return Whether this server has a flag set.
     */
    public boolean hasGlobalFlag(String key) {
        return this.flags.containsKey(key);
    }

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
    @SuppressWarnings("unchecked")
    public <T> T getGlobalFlag(String key) {
        Object value = this.flags.get(key);
        if(value == null) {
            return null;
        }

        try {
            return (T) value;
        } catch(ClassCastException e) {
            throw new IllegalStateException("Tried to get flag \"" + key + "\" as the wrong type. Actual type: " + value.getClass().getName());
        }
    }

    /**
     * Sets the value of a flag. The flag will be used in sessions if a session does
     * not contain a value for the flag.
     *
     * @param key   Key of the flag.
     * @param value Value to set the flag to.
     */
    public void setGlobalFlag(String key, Object value) {
        this.flags.put(key, value);
    }

    /**
     * Gets the listeners listening on this session.
     *
     * @return This server's listeners.
     */
    public List<ServerListener> getListeners() {
        return new ArrayList<ServerListener>(this.listeners);
    }

    /**
     * Adds a listener to this server.
     *
     * @param listener Listener to add.
     */
    public void addListener(ServerListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Removes a listener from this server.
     *
     * @param listener Listener to remove.
     */
    public void removeListener(ServerListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * Calls an event on the listeners of this server.
     *
     * @param event Event to call.
     */
    public void callEvent(ServerEvent event) {
        for(ServerListener listener : this.listeners) {
            event.call(listener);
        }
    }

    /**
     * Gets all sessions belonging to this server.
     *
     * @return Sessions belonging to this server.
     */
    public List<Session> getSessions() {
        return new ArrayList<Session>(this.sessions);
    }

    /**
     * Adds the given session to this server.
     *
     * @param session Session to add.
     */
    public void addSession(Session session) {
        this.sessions.add(session);
        this.callEvent(new SessionAddedEvent(this, session));
    }

    /**
     * Removes the given session from this server.
     *
     * @param session Session to remove.
     */
    public void removeSession(Session session) {
        this.sessions.remove(session);
        if(session.isConnected()) {
            session.disconnect("Connection closed.");
        }

        this.callEvent(new SessionRemovedEvent(this, session));
    }

    /**
     * Gets whether the server is listening.
     *
     * @return Whether the server is listening.
     */
    public boolean isListening() {
        return this.listener != null && this.listener.isListening();
    }

    /**
     * Closes the server.
     */
    public void close() {
        this.close(true);
    }

    /**
     * Closes the server.
     *
     * @param wait Whether to wait for the server to finish closing.
     */
    public void close(boolean wait) {
        this.callEvent(new ServerClosingEvent(this));
        for(Session session : this.getSessions()) {
            if(session.isConnected()) {
                session.disconnect("Server closed.");
            }
        }

        this.listener.close(wait, new Runnable() {
            @Override
            public void run() {
                callEvent(new ServerClosedEvent(Server.this));
            }
        });
    }
}
