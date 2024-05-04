package org.geysermc.mcprotocollib.network.codec;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.network.packet.Packet;

public interface PacketSerializer<T extends Packet, B extends CodecByteBuf> {

    void serialize(B buf, T packet);

    T deserialize(B buf, PacketDefinition<T, B> definition);
}
