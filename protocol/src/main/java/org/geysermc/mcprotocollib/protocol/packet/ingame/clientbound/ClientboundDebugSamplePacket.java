package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.RemoteDebugSampleType;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundDebugSamplePacket implements MinecraftPacket {
    private final long[] sample;
    private final RemoteDebugSampleType debugSampleType;

    public ClientboundDebugSamplePacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.sample = helper.readLongArray(in);
        this.debugSampleType = RemoteDebugSampleType.from(helper.readVarInt(in));
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeLongArray(out, this.sample);
        helper.writeVarInt(out, this.debugSampleType.ordinal());
    }
}
