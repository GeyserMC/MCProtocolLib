package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

@Data
@NoArgsConstructor
public class ClientboundStartConfigurationPacket implements MinecraftPacket {

    public ClientboundStartConfigurationPacket(ByteBuf in, MinecraftCodecHelper helper) {
    }

    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
    }
}
