package com.github.steveice10.packetlib.test;

import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.tcp.TcpClientSession;
import com.github.steveice10.packetlib.tcp.TcpServer;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class PingServerTest {
    public static void main(String[] args) {
        SecretKey key;
        try {
            KeyGenerator gen = KeyGenerator.getInstance("AES");
            gen.init(128);
            key = gen.generateKey();
        } catch(NoSuchAlgorithmException e) {
            System.err.println("AES algorithm not supported, exiting...");
            return;
        }

        Server server = new TcpServer("127.0.0.1", 25565, TestProtocol::new);
        server.addListener(new ServerListener(key));
        server.bind();

        Session client = new TcpClientSession("127.0.0.1", 25565, new TestProtocol(key));
        client.connect();
    }
}
