package org.spacehq.packetlib.test;

import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.crypt.AESEncryption;
import org.spacehq.packetlib.crypt.PacketEncryption;
import org.spacehq.packetlib.packet.DefaultPacketHeader;
import org.spacehq.packetlib.packet.PacketHeader;
import org.spacehq.packetlib.packet.PacketProtocol;

import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;

public class TestProtocol extends PacketProtocol {

	private PacketHeader header = new DefaultPacketHeader();
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
	public boolean needsPacketSizer() {
		return true;
	}

	@Override
	public boolean needsPacketEncryptor() {
		return true;
	}

	@Override
	public PacketHeader getPacketHeader() {
		return this.header;
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
