package org.geysermc.mcprotocollib.protocol.codec;

import org.geysermc.mcprotocollib.protocol.MinecraftConstants;
import org.geysermc.mcprotocollib.network.Server;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.codec.PacketCodecHelper;
import org.geysermc.mcprotocollib.network.codec.PacketDefinition;
import org.geysermc.mcprotocollib.network.packet.PacketHeader;
import org.geysermc.mcprotocollib.network.packet.PacketProtocol;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

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
    public PacketCodecHelper createHelper() {
        throw new UnsupportedOperationException("Not supported!");
    }

    @Override
    public void newClientSession(Session session, boolean transferring) {
        throw new UnsupportedOperationException("Not supported!");
    }

    @Override
    public void newServerSession(Server server, Session session) {
        throw new UnsupportedOperationException("Not supported!");
    }

    public static class Builder {
        private final Int2ObjectMap<PacketDefinition<? extends MinecraftPacket, MinecraftCodecHelper>> clientboundPackets = new Int2ObjectOpenHashMap<>();
        private final Int2ObjectMap<PacketDefinition<? extends MinecraftPacket, MinecraftCodecHelper>> serverboundPackets = new Int2ObjectOpenHashMap<>();

        private int nextClientboundId = 0x00;
        private int nextServerboundId = 0x00;

        public <T extends MinecraftPacket> Builder registerClientboundPacket(int id, Class<T> packetClass, PacketFactory<T, MinecraftCodecHelper> factory) {
            this.nextClientboundId = id;
            return registerClientboundPacket(packetClass, factory);
        }

        public <T extends MinecraftPacket> Builder registerClientboundPacket(Class<T> packetClass, PacketFactory<T, MinecraftCodecHelper> factory) {
            this.clientboundPackets.put(nextClientboundId, new PacketDefinition<>(nextClientboundId, packetClass, new MinecraftPacketSerializer<>(factory)));
            this.nextClientboundId++;
            return this;
        }

        public <T extends MinecraftPacket> Builder registerServerboundPacket(int id, Class<T> packetClass, PacketFactory<T, MinecraftCodecHelper> factory) {
            this.nextServerboundId = id;
            return registerServerboundPacket(packetClass, factory);
        }

        public <T extends MinecraftPacket> Builder registerServerboundPacket(Class<T> packetClass, PacketFactory<T, MinecraftCodecHelper> factory) {
            this.serverboundPackets.put(nextServerboundId, new PacketDefinition<>(nextServerboundId, packetClass, new MinecraftPacketSerializer<>(factory)));
            this.nextServerboundId++;
            return this;
        }

        public PacketStateCodec build() {
            PacketStateCodec codec = new PacketStateCodec();
            for (Int2ObjectMap.Entry<PacketDefinition<? extends MinecraftPacket, MinecraftCodecHelper>> entry : this.clientboundPackets.int2ObjectEntrySet()) {
                codec.registerClientbound(entry.getValue());
            }

            for (Int2ObjectMap.Entry<PacketDefinition<? extends MinecraftPacket, MinecraftCodecHelper>> entry : this.serverboundPackets.int2ObjectEntrySet()) {
                codec.registerServerbound(entry.getValue());
            }

            return codec;
        }
    }
}
