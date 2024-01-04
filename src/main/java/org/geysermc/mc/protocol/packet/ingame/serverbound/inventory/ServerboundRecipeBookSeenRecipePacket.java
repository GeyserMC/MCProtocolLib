package org.geysermc.mc.protocol.packet.ingame.serverbound.inventory;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ServerboundRecipeBookSeenRecipePacket implements MinecraftPacket {
    private final @NonNull String recipeId;

    public ServerboundRecipeBookSeenRecipePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.recipeId = helper.readString(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeString(out, this.recipeId);
    }
}
