package org.geysermc.mcprotocollib.network.test;

import org.geysermc.mcprotocollib.network.event.server.ServerAdapter;
import org.geysermc.mcprotocollib.network.event.server.ServerBoundEvent;
import org.geysermc.mcprotocollib.network.event.server.ServerClosedEvent;
import org.geysermc.mcprotocollib.network.event.server.ServerClosingEvent;
import org.geysermc.mcprotocollib.network.event.server.SessionAddedEvent;
import org.geysermc.mcprotocollib.network.event.server.SessionRemovedEvent;

import javax.crypto.SecretKey;

public class ServerListener extends ServerAdapter {
    private final SecretKey key;

    public ServerListener(SecretKey key) {
        this.key = key;
    }

    @Override
    public void serverBound(ServerBoundEvent event) {
        System.out.println("SERVER Bound: " + event.getServer().getHost() + ":" + event.getServer().getPort());
    }

    @Override
    public void serverClosing(ServerClosingEvent event) {
        System.out.println("CLOSING SERVER...");
    }

    @Override
    public void serverClosed(ServerClosedEvent event) {
        System.out.println("SERVER CLOSED");
    }

    @Override
    public void sessionAdded(SessionAddedEvent event) {
        System.out.println("SERVER Session Added: " + event.getSession().getHost() + ":" + event.getSession().getPort());
        ((TestProtocol) event.getSession().getPacketProtocol()).setSecretKey(this.key);
        event.getSession().enableEncryption(((TestProtocol) event.getSession().getPacketProtocol()).getEncryption());
    }

    @Override
    public void sessionRemoved(SessionRemovedEvent event) {
        System.out.println("SERVER Session Removed: " + event.getSession().getHost() + ":" + event.getSession().getPort());
        event.getServer().close(false);
    }
}
