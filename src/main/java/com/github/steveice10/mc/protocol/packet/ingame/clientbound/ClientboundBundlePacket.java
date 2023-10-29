package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;

import java.util.List;

public record ClientboundBundlePacket(List<MinecraftPacket> packets) implements MinecraftPacket {
    @Override
    public void serialize(ByteBuf buf, MinecraftCodecHelper helper) {
    }
}
