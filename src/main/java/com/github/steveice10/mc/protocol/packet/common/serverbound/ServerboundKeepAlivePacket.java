package com.github.steveice10.mc.protocol.packet.common.serverbound;

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
public class ServerboundKeepAlivePacket implements MinecraftPacket {
    private final long pingId;

    public ServerboundKeepAlivePacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.pingId = in.readLong();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeLong(this.pingId);
    }

}
