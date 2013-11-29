package ch.spacebase.packetlib.test;

import ch.spacebase.packetlib.Client;
import ch.spacebase.packetlib.Server;
import ch.spacebase.packetlib.Session;
import ch.spacebase.packetlib.packet.PacketProtocol;

public class TestProtocol extends PacketProtocol {

	public TestProtocol() {
		this.register(PingPacket.class);
	}
	
	@Override
	public void newClientSession(Client client, Session session) {
		session.addListener(new ClientSessionListener());
	}
	
	@Override
	public void newServer(Server server) {
		server.addListener(new ServerListener());
	}

	@Override
	public void newServerSession(Server server, Session session) {
		session.addListener(new ServerSessionListener());
	}

}
