package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

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
public class ClientboundSetTimePacket implements MinecraftPacket {
    private final long worldAge;
    private final long time;

    public ClientboundSetTimePacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.worldAge = in.readLong();
        this.time = in.readLong();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeLong(this.worldAge);
        out.writeLong(this.time);
    }
}
