package org.geysermc.mcprotocollib.protocol.data.game.recipe;

import org.geysermc.mcprotocollib.protocol.data.game.recipe.data.RecipeData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Recipe {
    private final @NonNull RecipeType type;
    private final @NonNull String identifier;
    private final RecipeData data;
}
