package org.spacehq.packetlib.test;

import org.spacehq.packetlib.event.session.ConnectedEvent;
import org.spacehq.packetlib.event.session.DisconnectedEvent;
import org.spacehq.packetlib.event.session.DisconnectingEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;

public class ClientSessionListener extends SessionAdapter {
    @Override
    public void packetReceived(PacketReceivedEvent event) {
        if(event.getPacket() instanceof PingPacket) {
            PingPacket packet = event.getPacket();
            System.out.println("CLIENT RECV: " + packet.getPingId());
            if(packet.getPingId().equals("exit")) {
                event.getSession().disconnect("Finished");
            }
        }
    }

    @Override
    public void connected(ConnectedEvent event) {
        event.getSession().send(new PingPacket("hello"));
        event.getSession().send(new PingPacket("exit"));
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
