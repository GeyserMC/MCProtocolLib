package org.geysermc.mcprotocollib.network.codec;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.network.packet.Packet;

public interface PacketSerializer<T extends Packet, H extends PacketCodecHelper> {

    void serialize(ByteBuf buf, H helper, T packet);

    T deserialize(ByteBuf buf, H helper, PacketDefinition<T, H> definition);
}
