package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundRecipeBookSeenRecipePacket implements MinecraftPacket {
    private final @NonNull String recipeId;

    public ServerboundRecipeBookSeenRecipePacket(MinecraftByteBuf buf) {
        this.recipeId = buf.readString();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeString(this.recipeId);
    }
}
