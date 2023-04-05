package com.github.steveice10.mc.protocol.event.session;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectingEvent;
import com.github.steveice10.packetlib.tcp.TcpClientSession;
import net.kyori.adventure.text.Component;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DisconnectingEventTest {
    @Test
    public void testGetSession() {
        Session session = new TcpClientSession("0.0.0.0",4500,new MinecraftProtocol());
        Component reason = Component.text("Server shutting down");
        Throwable cause = new IOException("Connection reset");
        DisconnectingEvent event = new DisconnectingEvent(session, reason, cause);
        assertEquals(session, event.getSession());
    }
    @Test
    public void testGetReason() {
        Session session = new TcpClientSession("0.0.0.0",4500,new MinecraftProtocol());
        Component reason = Component.text("Server shutting down");
        Throwable cause = new IOException("Connection reset");
        DisconnectingEvent event = new DisconnectingEvent(session, reason, cause);
        assertEquals(reason, event.getReason());
    }

    @Test
    public void testGetCause() {
        Session session = new TcpClientSession("0.0.0.0",4500,new MinecraftProtocol());
        Component reason = Component.text("Server shutting down");
        Throwable cause = new IOException("Connection reset");
        DisconnectingEvent event = new DisconnectingEvent(session, reason, cause);
        assertEquals(cause, event.getCause());
    }

    @Test
    public void testGetCauseIsNull() {
        Session session = new TcpClientSession("100.2.01.0",1255,new MinecraftProtocol());
        Component reason = Component.text("Server shutting down");
        DisconnectedEvent event = new DisconnectedEvent(session, reason, null);
        assertNull(event.getCause());
    }

}
