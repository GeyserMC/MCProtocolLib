package com.github.steveice10.mc.protocol.packet.login.server;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import com.github.steveice10.mc.protocol.util.CryptUtil;
import org.junit.Before;

import java.util.Random;

public class EncryptionRequestPacketTest extends PacketTest {
    @Before
    public void setup() {
        byte[] verifyToken = new byte[4];
        new Random().nextBytes(verifyToken);

        this.setPackets(new EncryptionRequestPacket("ServerID", CryptUtil.generateServerKeyPair().getPublic(), verifyToken));
    }
}
