package org.geysermc.mcprotocollib.protocol.codec;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.network.packet.Packet;

public interface MinecraftPacket extends Packet {

    void serialize(MinecraftByteBuf buf);
}
