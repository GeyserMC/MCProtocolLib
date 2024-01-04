package org.geysermc.mc.protocol.codec;

import org.geysermc.packetlib.packet.Packet;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public interface MinecraftPacket extends Packet {

    void serialize(ByteBuf buf, MinecraftCodecHelper helper) throws IOException;
}
