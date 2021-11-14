package com.github.steveice10.packetlib;

import com.github.steveice10.packetlib.event.server.*;
import com.github.steveice10.packetlib.packet.PacketProtocol;

import java.util.*;
import java.util.function.Supplier;

public abstract class AbstractServer implements Server {
    private final String host;
    private final int port;
    private final Supplier<? extends PacketProtocol> protocolSupplier;

    private final List<Session> sessions = new ArrayList<>();

    private final Map<String, Object> flags = new HashMap<>();
    private final List<ServerListener> listeners = new ArrayList<>();

    public AbstractServer(String host, int port, Supplier<? extends PacketProtocol> protocolSupplier) {
        this.host = host;
        this.port = port;
        this.protocolSupplier = protocolSupplier;
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
    public Supplier<? extends PacketProtocol> getPacketProtocol() {
        return this.protocolSupplier;
    }

    protected PacketProtocol createPacketProtocol() {
        return this.protocolSupplier.get();
    }

    @Override
    public Map<String, Object> getGlobalFlags() {
        return Collections.unmodifiableMap(this.flags);
    }

    @Override
    public boolean hasGlobalFlag(String key) {
        return this.flags.containsKey(key);
    }

    @Override
    public <T> T getGlobalFlag(String key) {
        return this.getGlobalFlag(key, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getGlobalFlag(String key, T def) {
        Object value = this.flags.get(key);
        if(value == null) {
            return def;
        }

        try {
            return (T) value;
        } catch(ClassCastException e) {
            throw new IllegalStateException("Tried to get flag \"" + key + "\" as the wrong type. Actual type: " + value.getClass().getName());
        }
    }

    @Override
    public void setGlobalFlag(String key, Object value) {
        this.flags.put(key, value);
    }

    @Override
    public List<ServerListener> getListeners() {
        return Collections.unmodifiableList(this.listeners);
    }

    @Override
    public void addListener(ServerListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(ServerListener listener) {
        this.listeners.remove(listener);
    }

    protected void callEvent(ServerEvent event) {
        for(ServerListener listener : this.listeners) {
            event.call(listener);
        }
    }

    @Override
    public List<Session> getSessions() {
        return new ArrayList<>(this.sessions);
    }

    public void addSession(Session session) {
        this.sessions.add(session);
        this.callEvent(new SessionAddedEvent(this, session));
    }

    public void removeSession(Session session) {
        this.sessions.remove(session);
        if(session.isConnected()) {
            session.disconnect("Connection closed.");
        }

        this.callEvent(new SessionRemovedEvent(this, session));
    }

    @Override
    public AbstractServer bind() {
        return this.bind(true);
    }

    @Override
    public AbstractServer bind(boolean wait) {
        return this.bind(wait, null);
    }

    @Override
    public AbstractServer bind(boolean wait, Runnable callback) {
        this.bindImpl(wait, () -> {
            callEvent(new ServerBoundEvent(AbstractServer.this));
            if(callback != null) {
                callback.run();
            }
        });

        return this;
    }

    protected abstract void bindImpl(boolean wait, Runnable callback);

    @Override
    public void close() {
        this.close(true);
    }

    @Override
    public void close(boolean wait) {
        this.close(wait, null);
    }

    @Override
    public void close(boolean wait, Runnable callback) {
        this.callEvent(new ServerClosingEvent(this));
        for(Session session : this.getSessions()) {
            if(session.isConnected()) {
                session.disconnect("Server closed.");
            }
        }

        this.closeImpl(wait, () -> {
            callEvent(new ServerClosedEvent(AbstractServer.this));
            if(callback != null) {
                callback.run();
            }
        });
    }

    protected abstract void closeImpl(boolean wait, Runnable callback);
}
