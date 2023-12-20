package com.github.steveice10.packetlib.codec;

import com.github.steveice10.packetlib.packet.Packet;
import io.netty.buffer.ByteBuf;

public interface PacketSerializer<T extends Packet, H extends PacketCodecHelper> {

    void serialize(ByteBuf buf, H helper, T packet);

    T deserialize(ByteBuf buf, H helper, PacketDefinition<T, H> definition);
}
