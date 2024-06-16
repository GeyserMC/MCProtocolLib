package org.geysermc.mcprotocollib.network.example;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.network.Server;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.codec.BasePacketCodecHelper;
import org.geysermc.mcprotocollib.network.codec.PacketCodecHelper;
import org.geysermc.mcprotocollib.network.codec.PacketDefinition;
import org.geysermc.mcprotocollib.network.codec.PacketSerializer;
import org.geysermc.mcprotocollib.network.crypt.AESEncryption;
import org.geysermc.mcprotocollib.network.crypt.PacketEncryption;
import org.geysermc.mcprotocollib.network.packet.DefaultPacketHeader;
import org.geysermc.mcprotocollib.network.packet.PacketHeader;
import org.geysermc.mcprotocollib.network.packet.PacketProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;

public class TestProtocol extends PacketProtocol {
    private static final Logger log = LoggerFactory.getLogger(TestProtocol.class);
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
            log.error("Failed to create encryption", e);
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
    public void newClientSession(Session session, boolean transferring) {
        session.addListener(new ClientSessionListener());
    }

    @Override
    public void newServerSession(Server server, Session session) {
        session.addListener(new ServerSessionListener());
    }
}
