package com.github.steveice10.mc.protocol.codec;

import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.packet.PacketDefinition;
import com.github.steveice10.packetlib.packet.PacketFactory;
import com.github.steveice10.packetlib.packet.PacketHeader;
import com.github.steveice10.packetlib.packet.PacketProtocol;

import java.util.HashMap;
import java.util.Map;

public class PacketStateCodec extends PacketProtocol {

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String getSRVRecordPrefix() {
        return MinecraftConstants.SRV_RECORD_PREFIX;
    }

    @Override
    public PacketHeader getPacketHeader() {
        return MinecraftConstants.PACKET_HEADER;
    }

    @Override
    public void newClientSession(Session session) {
        throw new UnsupportedOperationException("Not supported!");
    }

    @Override
    public void newServerSession(Server server, Session session) {
        throw new UnsupportedOperationException("Not supported!");
    }

    public static class Builder {
        private final Map<Integer, PacketDefinition<? extends Packet>> clientboundPackets = new HashMap<>();
        private final Map<Integer, PacketDefinition<? extends Packet>> serverboundPackets = new HashMap<>();

        public <T extends Packet> Builder registerClientboundPacket(int id, Class<T> packetClass, PacketFactory<T> factory) {
            this.clientboundPackets.put(id, new PacketDefinition<>(id, packetClass, factory));
            return this;
        }

        public <T extends Packet> Builder registerServerboundPacket(int id, Class<T> packetClass, PacketFactory<T> factory) {
            this.serverboundPackets.put(id, new PacketDefinition<>(id, packetClass, factory));
            return this;
        }

        public PacketStateCodec build() {
            PacketStateCodec codec = new PacketStateCodec();
            for (Map.Entry<Integer, PacketDefinition<? extends Packet>> entry : this.clientboundPackets.entrySet()) {
                codec.registerClientbound(entry.getValue());
            }

            for (Map.Entry<Integer, PacketDefinition<? extends Packet>> entry : this.serverboundPackets.entrySet()) {
                codec.registerServerbound(entry.getValue());
            }

            return codec;
        }
    }
}
