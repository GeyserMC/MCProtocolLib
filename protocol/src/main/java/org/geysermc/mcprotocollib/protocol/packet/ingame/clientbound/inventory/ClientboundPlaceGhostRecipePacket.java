package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.display.RecipeDisplay;

@Data
@With
@AllArgsConstructor
public class ClientboundPlaceGhostRecipePacket implements MinecraftPacket {
    private final int containerId;
    private final RecipeDisplay recipeDisplay;

    public ClientboundPlaceGhostRecipePacket(ByteBuf in) {
        this.containerId = MinecraftTypes.readVarInt(in);
        this.recipeDisplay = MinecraftTypes.readRecipeDisplay(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.containerId);
        MinecraftTypes.writeRecipeDisplay(out, this.recipeDisplay);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
