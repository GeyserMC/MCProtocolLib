package org.geysermc.mcprotocollib.network.codec;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.network.packet.Packet;

public interface PacketSerializer<T extends Packet> {

    void serialize(ByteBuf buf, T packet);

    T deserialize(ByteBuf buf, PacketDefinition<T> definition);
}
