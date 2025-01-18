package org.geysermc.mcprotocollib.protocol.codec;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import org.geysermc.mcprotocollib.network.codec.PacketDefinition;
import org.geysermc.mcprotocollib.network.codec.PacketSerializer;

@RequiredArgsConstructor
public class MinecraftPacketSerializer<T extends MinecraftPacket> implements PacketSerializer<T> {
    private final PacketFactory<T> factory;

    @Override
    public void serialize(ByteBuf buf, T packet) {
        packet.serialize(buf);
    }

    @Override
    public T deserialize(ByteBuf buf, PacketDefinition<T> definition) {
        return this.factory.construct(buf);
    }
}
