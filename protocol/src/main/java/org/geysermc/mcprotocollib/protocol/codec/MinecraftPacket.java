package org.geysermc.mcprotocollib.protocol.codec;

import org.geysermc.mcprotocollib.network.packet.Packet;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public interface MinecraftPacket extends Packet {

    void serialize(ByteBuf buf, MinecraftCodecHelper helper);
}
