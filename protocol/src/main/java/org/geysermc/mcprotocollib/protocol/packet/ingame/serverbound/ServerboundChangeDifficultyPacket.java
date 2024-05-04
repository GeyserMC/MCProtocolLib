package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.setting.Difficulty;

@Data
@With
@AllArgsConstructor
public class ServerboundChangeDifficultyPacket implements MinecraftPacket {
    private final @NonNull Difficulty difficulty;

    public ServerboundChangeDifficultyPacket(MinecraftByteBuf buf) {
        this.difficulty = Difficulty.from(buf.readByte());
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeByte(this.difficulty.ordinal());
    }
}
