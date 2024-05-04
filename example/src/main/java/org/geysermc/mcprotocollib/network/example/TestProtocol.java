package org.geysermc.mcprotocollib.network.example;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.network.Server;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.codec.BaseCodecByteBuf;
import org.geysermc.mcprotocollib.network.codec.ByteBufWrapper;
import org.geysermc.mcprotocollib.network.codec.CodecByteBuf;
import org.geysermc.mcprotocollib.network.codec.PacketDefinition;
import org.geysermc.mcprotocollib.network.codec.PacketSerializer;
import org.geysermc.mcprotocollib.network.crypt.AESEncryption;
import org.geysermc.mcprotocollib.network.crypt.PacketEncryption;
import org.geysermc.mcprotocollib.network.packet.DefaultPacketHeader;
import org.geysermc.mcprotocollib.network.packet.PacketHeader;
import org.geysermc.mcprotocollib.network.packet.PacketProtocol;

import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;

public class TestProtocol extends PacketProtocol<BaseCodecByteBuf> {
    private final PacketHeader header = new DefaultPacketHeader();
    private AESEncryption encrypt;

    @SuppressWarnings("unused")
    public TestProtocol() {
    }

    public TestProtocol(SecretKey key) {
        this.setSecretKey(key);
    }

    public ByteBufWrapper<BaseCodecByteBuf> getByteBufWrapper() {
        return BaseCodecByteBuf::new;
    }

    public void setSecretKey(SecretKey key) {
        this.register(0, PingPacket.class, new PacketSerializer<>() {
            @Override
            public void serialize(BaseCodecByteBuf buf, PingPacket packet) {
                buf.writeString(packet.getPingId());
            }

            @Override
            public PingPacket deserialize(BaseCodecByteBuf buf, PacketDefinition<PingPacket, BaseCodecByteBuf> definition) {
                return new PingPacket(buf);
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
    public void newClientSession(Session session, boolean transferring) {
        session.addListener(new ClientSessionListener());
    }

    @Override
    public void newServerSession(Server server, Session session) {
        session.addListener(new ServerSessionListener());
    }
}
