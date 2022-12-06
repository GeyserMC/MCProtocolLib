package com.github.steveice10.mc.protocol.data.game.recipe.data;

import com.github.steveice10.mc.protocol.data.game.recipe.CraftingBookCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class SimpleCraftingRecipeData implements RecipeData {
    private final @NonNull CraftingBookCategory category;
}
