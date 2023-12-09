package com.github.steveice10.mc.protocol.data.game.recipe.data;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.recipe.CraftingBookCategory;
import com.github.steveice10.mc.protocol.data.game.recipe.Ingredient;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public record CookedRecipeData(@NonNull String group, @NonNull CraftingBookCategory category, @NonNull Ingredient ingredient, @Nullable ItemStack result, float experience, int cookingTime) implements RecipeData {
}
