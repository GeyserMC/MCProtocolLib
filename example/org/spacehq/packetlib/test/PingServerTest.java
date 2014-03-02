package org.spacehq.packetlib.test;

import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class PingServerTest {

	public static void main(String[] args) {
		SecretKey key = null;
		try {
			KeyGenerator gen = KeyGenerator.getInstance("AES");
			gen.init(128);
			key = gen.generateKey();
		} catch(NoSuchAlgorithmException e) {
			System.err.println("AES algorithm not supported, exiting...");
			return;
		}

		Server server = new Server("127.0.0.1", 25565, TestProtocol.class, new TcpSessionFactory()).bind();
		server.addListener(new ServerListener(key));

		Client client = new Client("127.0.0.1", 25565, new TestProtocol(key), new TcpSessionFactory());
		client.getSession().connect();
	}

}
