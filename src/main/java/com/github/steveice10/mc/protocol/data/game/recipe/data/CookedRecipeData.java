package com.github.steveice10.mc.protocol.data.game.recipe.data;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.recipe.CraftingBookCategory;
import com.github.steveice10.mc.protocol.data.game.recipe.Ingredient;
import org.jetbrains.annotations.NotNull;

public record CookedRecipeData(@NotNull String group, @NotNull CraftingBookCategory category,
                               @NotNull Ingredient ingredient, ItemStack result,
                               float experience, int cookingTime) implements RecipeData {
}
