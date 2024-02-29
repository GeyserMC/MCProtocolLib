package org.geysermc.mcprotocollib.network.example;

import org.geysermc.mcprotocollib.network.Server;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.codec.BasePacketCodecHelper;
import org.geysermc.mcprotocollib.network.codec.PacketCodecHelper;
import org.geysermc.mcprotocollib.network.codec.PacketDefinition;
import org.geysermc.mcprotocollib.network.codec.PacketSerializer;
import org.geysermc.mcprotocollib.network.packet.DefaultPacketHeader;
import org.geysermc.mcprotocollib.network.packet.PacketHeader;
import org.geysermc.mcprotocollib.network.packet.PacketProtocol;
import io.netty.buffer.ByteBuf;

import javax.crypto.SecretKey;

public class TestProtocol extends PacketProtocol {
    private final PacketHeader header = new DefaultPacketHeader();
    private final SecretKey key;

    public TestProtocol(SecretKey key) {
        this.key = key;
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
    }

    public PacketCodecHelper createHelper() {
        return new BasePacketCodecHelper();
    }

    @Override
    public String getSRVRecordPrefix() {
        return "_test";
    }

    @Override
    public PacketHeader getPacketHeader() {
        return this.header;
    }

    @Override
    public void newClientSession(Session session) {
        session.addListener(new ClientSessionListener(key));
    }

    @Override
    public void newServerSession(Server server, Session session) {
        session.addListener(new ServerSessionListener(key));
    }
}
