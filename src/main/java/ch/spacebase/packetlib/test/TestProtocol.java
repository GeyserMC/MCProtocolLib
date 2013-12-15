package ch.spacebase.packetlib.test;

import java.security.GeneralSecurityException;

import javax.crypto.SecretKey;

import ch.spacebase.packetlib.Client;
import ch.spacebase.packetlib.Server;
import ch.spacebase.packetlib.Session;
import ch.spacebase.packetlib.crypt.AESEncryption;
import ch.spacebase.packetlib.crypt.PacketEncryption;
import ch.spacebase.packetlib.packet.PacketProtocol;

public class TestProtocol extends PacketProtocol {

	private AESEncryption encrypt;
	
	@SuppressWarnings("unused")
	private TestProtocol() {
	}
	
	public TestProtocol(SecretKey key) {
		this.setSecretKey(key);
	}
	
	public void setSecretKey(SecretKey key) {
		this.register(0, PingPacket.class);
		try {
			this.encrypt = new AESEncryption(key);
		} catch(GeneralSecurityException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public PacketEncryption getEncryption() {
		return this.encrypt;
	}
	
	@Override
	public void newClientSession(Client client, Session session) {
		session.addListener(new ClientSessionListener());
	}

	@Override
	public void newServerSession(Server server, Session session) {
		session.addListener(new ServerSessionListener());
	}

}
