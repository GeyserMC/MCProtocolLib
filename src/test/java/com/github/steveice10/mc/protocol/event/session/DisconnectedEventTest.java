package com.github.steveice10.mc.protocol.event.session;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.tcp.TcpClientSession;
import org.junit.Test;

import java.io.IOException;
import net.kyori.adventure.text.Component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DisconnectedEventTest {

    @Test
    public void testGetSession() {
        Session session = new TcpClientSession("0.0.0.0",4500,new MinecraftProtocol());
        Component reason = Component.text("Server shutting down");
        Throwable cause = new IOException("Connection reset");
        DisconnectedEvent event = new DisconnectedEvent(session, reason, cause);
        assertEquals(session, event.getSession());
    }

    @Test
    public void testGetReason() {
        Session session = new TcpClientSession("0.0.0.0",4500,new MinecraftProtocol());
        Component reason = Component.text("Server shutting down");
        Throwable cause = new IOException("Connection reset");
        DisconnectedEvent event = new DisconnectedEvent(session, reason, cause);
        assertEquals(reason, event.getReason());
    }

    @Test
    public void testGetCause() {
        Session session = new TcpClientSession("120.0.0.0",4500,new MinecraftProtocol());
        Component reason = Component.text("Server shutting down");
        Throwable cause = new IOException("Connection reset");
        DisconnectedEvent event = new DisconnectedEvent(session, reason, cause);
        assertEquals(cause, event.getCause());
    }

        @Test
        public void testGetCauseIsNull() {
            Session session = new TcpClientSession("0.0.1.1",4555,new MinecraftProtocol());
            Component reason = Component.text("Server shutting down");
            DisconnectedEvent event = new DisconnectedEvent(session, reason, null);
            assertNull(event.getCause());
        }
}
