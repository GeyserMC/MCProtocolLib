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
public class ClientboundSetBorderLerpSizePacket implements MinecraftPacket {
    private final double oldSize;
    private final double newSize;
    private final long lerpTime;

    public ClientboundSetBorderLerpSizePacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.oldSize = in.readDouble();
        this.newSize = in.readDouble();
        this.lerpTime = helper.readVarLong(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeDouble(this.oldSize);
        out.writeDouble(this.newSize);
        helper.writeVarLong(out, this.lerpTime);
    }
}
