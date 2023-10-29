package com.github.steveice10.mc.protocol.packet.ingame.serverbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.NotNull;

@Data
@With
@AllArgsConstructor
public class ServerboundChangeDifficultyPacket implements MinecraftPacket {
    private final @NotNull Difficulty difficulty;

    public ServerboundChangeDifficultyPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.difficulty = Difficulty.from(in.readByte());
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeByte(this.difficulty.ordinal());
    }
}
