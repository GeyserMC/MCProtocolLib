package com.github.steveice10.mc.protocol.codec;

import com.github.steveice10.packetlib.packet.Packet;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public interface MinecraftPacket extends Packet {

    void serialize(ByteBuf buf, MinecraftCodecHelper helper) throws IOException;
}
