package com.github.steveice10.mc.protocol.packet.ingame.serverbound;

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
public class ServerboundDebugSampleSubscriptionPacket implements MinecraftPacket {
    private final RemoteDebugSampleType debugSampleType;

    public ServerboundDebugSampleSubscriptionPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.debugSampleType = RemoteDebugSampleType.from(helper.readVarInt(in));
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.debugSampleType.ordinal());
    }
}
