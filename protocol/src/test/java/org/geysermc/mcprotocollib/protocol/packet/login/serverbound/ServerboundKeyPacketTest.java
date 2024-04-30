package org.geysermc.mcprotocollib.protocol.packet.login.serverbound;

import org.geysermc.mcprotocollib.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerboundKeyPacketTest extends PacketTest {
    private KeyPair keyPair;
    private SecretKey secretKey;
    private ServerboundKeyPacket packet;
    private byte[] verifyToken;

    @BeforeEach
    public void setup() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024);
            this.keyPair = keyPairGen.generateKeyPair();

            KeyGenerator secretKeyGen = KeyGenerator.getInstance("AES");
            secretKeyGen.init(128);
            this.secretKey = secretKeyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Failed to generate test keys.", e);
        }

        this.verifyToken = new byte[4];
        new Random().nextBytes(this.verifyToken);

        this.packet = new ServerboundKeyPacket(this.keyPair.getPublic(), this.secretKey, this.verifyToken);
        this.setPackets(this.packet);
    }

    @Test
    public void testEncryptionResponsePacketGetters() {
        assertEquals(this.secretKey, this.packet.getSecretKey(this.keyPair.getPrivate()), "Secret key does not match.");
        assertArrayEquals(this.verifyToken, this.packet.getEncryptedChallenge(this.keyPair.getPrivate()), "Verify token does not match.");
    }
}
