package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.util.List;

@Data
@AllArgsConstructor
public class ClientboundBundlePacket implements MinecraftPacket {
    private final List<MinecraftPacket> packets;

    @Override
    public void serialize(ByteBuf buf, MinecraftCodecHelper helper) throws IOException {
    }
}
