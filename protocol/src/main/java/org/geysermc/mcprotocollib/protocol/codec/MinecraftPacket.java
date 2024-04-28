package org.geysermc.mcprotocollib.protocol.codec;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.network.packet.Packet;

public interface MinecraftPacket extends Packet {

    void serialize(ByteBuf buf, MinecraftCodecHelper helper);
}
