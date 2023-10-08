package com.github.steveice10.mc.protocol.packet.login.clientbound;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class ClientboundHelloPacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024);
            keyPair = keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Failed to generate test key pair.", e);
        }

        byte[] verifyToken = new byte[4];
        new Random().nextBytes(verifyToken);

        this.setPackets(new ClientboundHelloPacket("ServerID", keyPair.getPublic(), verifyToken));
    }
}
