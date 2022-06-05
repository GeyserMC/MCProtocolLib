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
public class ClientboundSetBorderCenterPacket implements MinecraftPacket {
    private final double newCenterX;
    private final double newCenterZ;

    public ClientboundSetBorderCenterPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.newCenterX = in.readDouble();
        this.newCenterZ = in.readDouble();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeDouble(this.newCenterX);
        out.writeDouble(this.newCenterZ);
    }
}
