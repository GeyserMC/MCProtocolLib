package org.geysermc.mcprotocollib.protocol.data.game.recipe.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.Ingredient;

@Data
@AllArgsConstructor
public class SmithingTrimRecipeData implements RecipeData {
    private final @NonNull Ingredient template;
    private final @NonNull Ingredient base;
    private final @NonNull Ingredient addition;
}
