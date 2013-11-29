package ch.spacebase.packetlib.test;

import ch.spacebase.packetlib.event.session.DisconnectedEvent;
import ch.spacebase.packetlib.event.session.DisconnectingEvent;
import ch.spacebase.packetlib.event.session.PacketReceivedEvent;
import ch.spacebase.packetlib.event.session.SessionAdapter;

public class ServerSessionListener extends SessionAdapter {

	@Override
	public void packetReceived(PacketReceivedEvent event) {
		if(event.getPacket() instanceof PingPacket) {
			System.out.println("SERVER RECV: " + ((PingPacket) event.getPacket()).getPingId());
			event.getSession().send(event.getPacket());
		}
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
