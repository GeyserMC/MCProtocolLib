package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

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
public class ClientboundTickingStatePacket implements MinecraftPacket {

    private final float tickRate;
    private final boolean isFrozen;

    public ClientboundTickingStatePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.tickRate = in.readFloat();
        this.isFrozen = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeFloat(tickRate);
        out.writeBoolean(isFrozen);
    }
}
