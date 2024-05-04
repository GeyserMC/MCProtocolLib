package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

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
public class ClientboundChangeDifficultyPacket implements MinecraftPacket {
    private final @NonNull Difficulty difficulty;
    private final boolean difficultyLocked;

    public ClientboundChangeDifficultyPacket(MinecraftByteBuf buf) {
        this.difficulty = Difficulty.from(buf.readUnsignedByte());
        this.difficultyLocked = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeByte(this.difficulty.ordinal());
        buf.writeBoolean(this.difficultyLocked);
    }
}
