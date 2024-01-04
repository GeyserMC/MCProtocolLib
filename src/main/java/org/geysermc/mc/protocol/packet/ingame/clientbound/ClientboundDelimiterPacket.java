package org.geysermc.mc.protocol.packet.ingame.clientbound;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;

public class ClientboundDelimiterPacket implements MinecraftPacket {
    public ClientboundDelimiterPacket(ByteBuf in, MinecraftCodecHelper helper) {
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
    }
}
