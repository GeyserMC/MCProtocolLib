package com.github.steveice10.mc.protocol.codec;

import com.github.steveice10.packetlib.codec.PacketDefinition;
import com.github.steveice10.packetlib.codec.PacketSerializer;
import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class MinecraftPacketSerializer<T extends MinecraftPacket> implements PacketSerializer<T, MinecraftCodecHelper> {
    private final PacketFactory<T, MinecraftCodecHelper> factory;

    @Override
    public void serialize(ByteBuf buf, MinecraftCodecHelper helper, T packet) throws IOException {
        packet.serialize(buf, helper);
    }

    @Override
    public T deserialize(ByteBuf buf, MinecraftCodecHelper helper, PacketDefinition<T, MinecraftCodecHelper> definition) throws IOException {
        return this.factory.construct(buf, helper);
    }
}
