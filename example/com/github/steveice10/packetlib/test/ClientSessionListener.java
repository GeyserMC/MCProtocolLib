package com.github.steveice10.packetlib.test;

import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectingEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.PacketSentEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;

public class ClientSessionListener extends SessionAdapter {
    @Override
    public void packetReceived(PacketReceivedEvent event) {
        if(event.getPacket() instanceof PingPacket) {
            PingPacket packet = event.getPacket();

            System.out.println("CLIENT Received: " + packet.getPingId());

            if(packet.getPingId().equals("hello")) {
                event.getSession().send(new PingPacket("exit"));
            } else if(packet.getPingId().equals("exit")) {
                event.getSession().disconnect("Finished");
            }
        }
    }

    @Override
    public void packetSent(PacketSentEvent event) {
        if(event.getPacket() instanceof PingPacket) {
            System.out.println("CLIENT Sent: " + event.<PingPacket>getPacket().getPingId());
        }
    }

    @Override
    public void connected(ConnectedEvent event) {
        System.out.println("CLIENT Connected");

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
