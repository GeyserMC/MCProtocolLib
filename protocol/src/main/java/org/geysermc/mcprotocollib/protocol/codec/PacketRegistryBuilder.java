package org.geysermc.mcprotocollib.protocol.codec;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.geysermc.mcprotocollib.network.codec.PacketDefinition;
import org.geysermc.mcprotocollib.network.packet.PacketRegistry;

public class PacketRegistryBuilder {
    private PacketRegistryBuilder() {
    }

    public static PacketRegistryBuilder builder() {
        return new PacketRegistryBuilder();
    }

    private final Int2ObjectMap<PacketDefinition<? extends MinecraftPacket, MinecraftCodecHelper>> clientboundPackets = new Int2ObjectOpenHashMap<>();
    private final Int2ObjectMap<PacketDefinition<? extends MinecraftPacket, MinecraftCodecHelper>> serverboundPackets = new Int2ObjectOpenHashMap<>();

    private int nextClientboundId = 0x00;
    private int nextServerboundId = 0x00;

    public <T extends MinecraftPacket> PacketRegistryBuilder registerClientboundPacket(int id, Class<T> packetClass, PacketFactory<T, MinecraftCodecHelper> factory) {
        this.nextClientboundId = id;
        return registerClientboundPacket(packetClass, factory);
    }

    public <T extends MinecraftPacket> PacketRegistryBuilder registerClientboundPacket(Class<T> packetClass, PacketFactory<T, MinecraftCodecHelper> factory) {
        this.clientboundPackets.put(nextClientboundId, new PacketDefinition<>(nextClientboundId, packetClass, new MinecraftPacketSerializer<>(factory)));
        this.nextClientboundId++;
        return this;
    }

    public <T extends MinecraftPacket> PacketRegistryBuilder registerServerboundPacket(int id, Class<T> packetClass, PacketFactory<T, MinecraftCodecHelper> factory) {
        this.nextServerboundId = id;
        return registerServerboundPacket(packetClass, factory);
    }

    public <T extends MinecraftPacket> PacketRegistryBuilder registerServerboundPacket(Class<T> packetClass, PacketFactory<T, MinecraftCodecHelper> factory) {
        this.serverboundPackets.put(nextServerboundId, new PacketDefinition<>(nextServerboundId, packetClass, new MinecraftPacketSerializer<>(factory)));
        this.nextServerboundId++;
        return this;
    }

    public PacketRegistry build() {
        PacketRegistry codec = new PacketRegistry();
        for (Int2ObjectMap.Entry<PacketDefinition<? extends MinecraftPacket, MinecraftCodecHelper>> entry : this.clientboundPackets.int2ObjectEntrySet()) {
            codec.registerClientbound(entry.getValue());
        }

        for (Int2ObjectMap.Entry<PacketDefinition<? extends MinecraftPacket, MinecraftCodecHelper>> entry : this.serverboundPackets.int2ObjectEntrySet()) {
            codec.registerServerbound(entry.getValue());
        }

        return codec;
    }
}
