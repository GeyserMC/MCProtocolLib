package org.geysermc.mc.protocol.packet.ingame.serverbound;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import org.geysermc.mc.protocol.data.game.setting.Difficulty;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ServerboundChangeDifficultyPacket implements MinecraftPacket {
    private final @NonNull Difficulty difficulty;

    public ServerboundChangeDifficultyPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.difficulty = Difficulty.from(in.readByte());
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeByte(this.difficulty.ordinal());
    }
}
