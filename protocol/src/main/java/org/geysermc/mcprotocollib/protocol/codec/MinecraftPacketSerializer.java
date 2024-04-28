package org.geysermc.mcprotocollib.protocol.codec;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import org.geysermc.mcprotocollib.network.codec.PacketDefinition;
import org.geysermc.mcprotocollib.network.codec.PacketSerializer;

@RequiredArgsConstructor
public class MinecraftPacketSerializer<T extends MinecraftPacket> implements PacketSerializer<T, MinecraftCodecHelper> {
    private final PacketFactory<T, MinecraftCodecHelper> factory;

    @Override
    public void serialize(ByteBuf buf, MinecraftCodecHelper helper, T packet) {
        packet.serialize(buf, helper);
    }

    @Override
    public T deserialize(ByteBuf buf, MinecraftCodecHelper helper, PacketDefinition<T, MinecraftCodecHelper> definition) {
        return this.factory.construct(buf, helper);
    }
}
