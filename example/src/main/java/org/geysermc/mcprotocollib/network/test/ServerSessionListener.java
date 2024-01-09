package org.geysermc.mcprotocollib.network.test;

import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.ConnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.DisconnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.DisconnectingEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.packet.Packet;

public class ServerSessionListener extends SessionAdapter {
    @Override
    public void packetReceived(Session session, Packet packet) {
        if (packet instanceof PingPacket) {
            System.out.println("SERVER Received: " + ((PingPacket) packet).getPingId());
            session.send(packet);
        }
    }

    @Override
    public void packetSent(Session session, Packet packet) {
        if (packet instanceof PingPacket) {
            System.out.println("SERVER Sent: " + ((PingPacket) packet).getPingId());
        }
    }

    @Override
    public void connected(ConnectedEvent event) {
        System.out.println("SERVER Connected");
    }

    @Override
    public void disconnecting(DisconnectingEvent event) {
        System.out.println("SERVER Disconnecting: " + event.getReason());
    }

    @Override
    public void disconnected(DisconnectedEvent event) {
        System.out.println("SERVER Disconnected: " + event.getReason());
    }
}
