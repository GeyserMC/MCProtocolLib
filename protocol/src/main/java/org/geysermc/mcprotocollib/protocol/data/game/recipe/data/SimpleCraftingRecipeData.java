package org.geysermc.mcprotocollib.protocol.data.game.recipe.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.CraftingBookCategory;

@Data
@AllArgsConstructor
public class SimpleCraftingRecipeData implements RecipeData {
    private final @NonNull CraftingBookCategory category;
}
