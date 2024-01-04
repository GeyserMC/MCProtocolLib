package com.github.steveice10.mc.protocol.packet.common.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ClientboundKeepAlivePacket implements MinecraftPacket {
    private final long pingId;

    public ClientboundKeepAlivePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.pingId = in.readLong();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeLong(this.pingId);
    }
}
