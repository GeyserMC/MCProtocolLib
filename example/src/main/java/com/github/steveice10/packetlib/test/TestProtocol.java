package com.github.steveice10.packetlib.test;

import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.codec.BasePacketCodecHelper;
import com.github.steveice10.packetlib.codec.PacketCodecHelper;
import com.github.steveice10.packetlib.codec.PacketDefinition;
import com.github.steveice10.packetlib.codec.PacketSerializer;
import com.github.steveice10.packetlib.crypt.AESEncryption;
import com.github.steveice10.packetlib.crypt.PacketEncryption;
import com.github.steveice10.packetlib.packet.DefaultPacketHeader;
import com.github.steveice10.packetlib.packet.PacketHeader;
import com.github.steveice10.packetlib.packet.PacketProtocol;
import io.netty.buffer.ByteBuf;

import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;

public class TestProtocol extends PacketProtocol {
    private final PacketHeader header = new DefaultPacketHeader();
    private AESEncryption encrypt;

    @SuppressWarnings("unused")
    public TestProtocol() {
    }

    public TestProtocol(SecretKey key) {
        this.setSecretKey(key);
    }

    public PacketCodecHelper createHelper() {
        return new BasePacketCodecHelper();
    }

    public void setSecretKey(SecretKey key) {
        this.register(0, PingPacket.class, new PacketSerializer<>() {
            @Override
            public void serialize(ByteBuf buf, PacketCodecHelper helper, PingPacket packet) {
                helper.writeString(buf, packet.getPingId());
            }

            @Override
            public PingPacket deserialize(ByteBuf buf, PacketCodecHelper helper, PacketDefinition<PingPacket, PacketCodecHelper> definition) {
                return new PingPacket(buf, helper);
            }
        });

        try {
            this.encrypt = new AESEncryption(key);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getSRVRecordPrefix() {
        return "_test";
    }

    @Override
    public PacketHeader getPacketHeader() {
        return this.header;
    }

    public PacketEncryption getEncryption() {
        return this.encrypt;
    }

    @Override
    public void newClientSession(Session session) {
        session.addListener(new ClientSessionListener());
    }

    @Override
    public void newServerSession(Server server, Session session) {
        session.addListener(new ServerSessionListener());
    }
}
