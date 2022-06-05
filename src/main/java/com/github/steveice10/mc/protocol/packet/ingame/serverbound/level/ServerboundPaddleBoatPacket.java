package com.github.steveice10.mc.protocol.packet.ingame.serverbound.level;

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
public class ServerboundPaddleBoatPacket implements MinecraftPacket {
    private final boolean rightPaddleTurning;
    private final boolean leftPaddleTurning;

    public ServerboundPaddleBoatPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.rightPaddleTurning = in.readBoolean();
        this.leftPaddleTurning = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeBoolean(this.rightPaddleTurning);
        out.writeBoolean(this.leftPaddleTurning);
    }
}
