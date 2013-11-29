package ch.spacebase.packetlib.test;

import ch.spacebase.packetlib.Client;
import ch.spacebase.packetlib.Server;
import ch.spacebase.packetlib.netty.NettySessionFactory;

public class PingServerTest {

	public static void main(String[] args) {
		new Server("127.0.0.1", 25565, new TestProtocol(), new NettySessionFactory()).bind();
		new Client("127.0.0.1", 25565, new TestProtocol(), new NettySessionFactory()).connect();
	}

}
