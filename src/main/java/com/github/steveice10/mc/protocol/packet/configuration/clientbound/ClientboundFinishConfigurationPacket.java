package com.github.steveice10.mc.protocol.packet.configuration.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientboundFinishConfigurationPacket implements MinecraftPacket {
    public ClientboundFinishConfigurationPacket(ByteBuf in, MinecraftCodecHelper helper) {
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
    }
}
