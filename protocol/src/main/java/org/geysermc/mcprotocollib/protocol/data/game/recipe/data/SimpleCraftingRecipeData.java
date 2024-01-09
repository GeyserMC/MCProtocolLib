package org.geysermc.mcprotocollib.protocol.data.game.recipe.data;

import org.geysermc.mcprotocollib.protocol.data.game.recipe.CraftingBookCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class SimpleCraftingRecipeData implements RecipeData {
    private final @NonNull CraftingBookCategory category;
}
