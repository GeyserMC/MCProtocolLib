package org.geysermc.mcprotocollib.protocol.codec;

import lombok.RequiredArgsConstructor;
import org.geysermc.mcprotocollib.network.codec.PacketDefinition;
import org.geysermc.mcprotocollib.network.codec.PacketSerializer;

@RequiredArgsConstructor
public class MinecraftPacketSerializer<T extends MinecraftPacket> implements PacketSerializer<T, MinecraftByteBuf> {
    private final PacketFactory<T, MinecraftByteBuf> factory;

    @Override
    public void serialize(MinecraftByteBuf buf, T packet) {
        packet.serialize(buf);
    }

    @Override
    public T deserialize(MinecraftByteBuf buf, PacketDefinition<T, MinecraftByteBuf> definition) {
        return this.factory.construct(buf);
    }
}
