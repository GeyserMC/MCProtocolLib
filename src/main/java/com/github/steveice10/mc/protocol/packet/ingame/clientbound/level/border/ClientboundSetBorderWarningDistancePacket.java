package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.border;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundSetBorderWarningDistancePacket implements MinecraftPacket {
    private final int warningBlocks;

    public ClientboundSetBorderWarningDistancePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.warningBlocks = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.warningBlocks);
    }
}
