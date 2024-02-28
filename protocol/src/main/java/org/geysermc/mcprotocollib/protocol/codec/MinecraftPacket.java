package org.geysermc.mcprotocollib.protocol.codec;

import org.geysermc.mcprotocollib.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public interface MinecraftPacket extends Packet {

    void serialize(ByteBuf buf, MinecraftCodecHelper helper);
}
