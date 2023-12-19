package com.github.steveice10.packetlib.test;

import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectingEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;

public class ClientSessionListener extends SessionAdapter {
    @Override
    public void packetReceived(Session session, Packet packet) {
        if (packet instanceof PingPacket) {
            String id = ((PingPacket) packet).getPingId();

            System.out.println("CLIENT Received: " + id);

            if (id.equals("hello")) {
                session.send(new PingPacket("exit"));
            } else if (id.equals("exit")) {
                session.disconnect("Finished");
            }
        }
    }

    @Override
    public void packetSent(Session session, Packet packet) {
        if (packet instanceof PingPacket) {
            System.out.println("CLIENT Sent: " + ((PingPacket) packet).getPingId());
        }
    }

    @Override
    public void connected(ConnectedEvent event) {
        System.out.println("CLIENT Connected");

        event.getSession().enableEncryption(((TestProtocol) event.getSession().getPacketProtocol()).getEncryption());
        event.getSession().send(new PingPacket("hello"));
    }

    @Override
    public void disconnecting(DisconnectingEvent event) {
        System.out.println("CLIENT Disconnecting: " + event.getReason());
    }

    @Override
    public void disconnected(DisconnectedEvent event) {
        System.out.println("CLIENT Disconnected: " + event.getReason());
    }
}
