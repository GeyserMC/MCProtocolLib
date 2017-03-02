package com.github.steveice10.packetlib.test;

import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectingEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.PacketSentEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;

public class ServerSessionListener extends SessionAdapter {
    @Override
    public void packetReceived(PacketReceivedEvent event) {
        if(event.getPacket() instanceof PingPacket) {
            System.out.println("SERVER Received: " + event.<PingPacket>getPacket().getPingId());
            event.getSession().send(event.getPacket());
        }
    }

    @Override
    public void packetSent(PacketSentEvent event) {
        if(event.getPacket() instanceof PingPacket) {
            System.out.println("SERVER Sent: " + event.<PingPacket>getPacket().getPingId());
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
