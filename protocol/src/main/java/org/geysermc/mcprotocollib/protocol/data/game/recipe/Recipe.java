package org.geysermc.mcprotocollib.protocol.data.game.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.geysermc.mcprotocollib.protocol.data.game.ResourceLocation;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.data.RecipeData;

@Data
@AllArgsConstructor
public class Recipe {
    private final @NonNull RecipeType type;
    private final @NonNull ResourceLocation identifier;
    private final RecipeData data;
}
