package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundRecipeBookRemovePacket implements MinecraftPacket {
    private final int[] recipes;

    public ClientboundRecipeBookRemovePacket(ByteBuf in, MinecraftCodecHelper helper) {
        int recipeCount = helper.readVarInt(in);
        int[] recipes = new int[recipeCount];
        for (int index = 0; index < recipeCount; index++) {
            recipes[index] = helper.readVarInt(in);
        }
        this.recipes = recipes;
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.recipes.length);
        for (int recipe : recipes) {
            helper.writeVarInt(out, recipe);
        }
    }
}
