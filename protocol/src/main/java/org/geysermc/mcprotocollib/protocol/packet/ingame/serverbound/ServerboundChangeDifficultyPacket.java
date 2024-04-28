package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.setting.Difficulty;

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
