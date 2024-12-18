package org.geysermc.mcprotocollib.network.example;

import org.geysermc.mcprotocollib.network.ClientSession;
import org.geysermc.mcprotocollib.network.Server;
import org.geysermc.mcprotocollib.network.session.ClientNetworkSession;
import org.geysermc.mcprotocollib.network.server.NetworkServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;

public class PingServerTest {
    private static final Logger log = LoggerFactory.getLogger(PingServerTest.class);

    public static void main(String[] args) {
        SecretKey key;
        try {
            KeyGenerator gen = KeyGenerator.getInstance("AES");
            gen.init(128);
            key = gen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            log.error("AES algorithm not supported, exiting...");
            return;
        }

        Server server = new NetworkServer(new InetSocketAddress("127.0.0.1", 25565), TestProtocol::new);
        server.addListener(new ServerListener(key));
        server.bind();

        ClientSession client = new ClientNetworkSession(new InetSocketAddress("127.0.0.1", 25565), new TestProtocol(key));
        client.connect();
    }
}
