package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundChangeDifficultyPacket implements MinecraftPacket {
    private final @NonNull Difficulty difficulty;
    private final boolean difficultyLocked;

    public ClientboundChangeDifficultyPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.difficulty = Difficulty.from(in.readUnsignedByte());
        this.difficultyLocked = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeByte(this.difficulty.ordinal());
        out.writeBoolean(this.difficultyLocked);
    }
}
