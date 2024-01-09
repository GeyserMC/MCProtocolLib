package org.geysermc.mcprotocollib.network.codec;

import org.geysermc.mcprotocollib.network.packet.Packet;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public interface PacketSerializer<T extends Packet, H extends PacketCodecHelper> {

    void serialize(ByteBuf buf, H helper, T packet) throws IOException;

    T deserialize(ByteBuf buf, H helper, PacketDefinition<T, H> definition) throws IOException;
}
