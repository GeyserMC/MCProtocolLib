package com.github.steveice10.mc.protocol.data.game.recipe.data;

import com.github.steveice10.mc.protocol.data.game.recipe.CraftingBookCategory;
import org.jetbrains.annotations.NotNull;

public record SimpleCraftingRecipeData(@NotNull CraftingBookCategory category) implements RecipeData {
}
