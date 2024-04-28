package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

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
public class ClientboundChangeDifficultyPacket implements MinecraftPacket {
    private final @NonNull Difficulty difficulty;
    private final boolean difficultyLocked;

    public ClientboundChangeDifficultyPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.difficulty = Difficulty.from(in.readUnsignedByte());
        this.difficultyLocked = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeByte(this.difficulty.ordinal());
        out.writeBoolean(this.difficultyLocked);
    }
}
