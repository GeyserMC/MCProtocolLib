package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundRecipeBookRemovePacket implements MinecraftPacket {
    private final int[] recipes;

    public ClientboundRecipeBookRemovePacket(ByteBuf in) {
        int recipeCount = MinecraftTypes.readVarInt(in);
        int[] recipes = new int[recipeCount];
        for (int index = 0; index < recipeCount; index++) {
            recipes[index] = MinecraftTypes.readVarInt(in);
        }
        this.recipes = recipes;
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.recipes.length);
        for (int recipe : recipes) {
            MinecraftTypes.writeVarInt(out, recipe);
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
