package com.github.steveice10.mc.protocol.data.game.recipe.data;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.recipe.Ingredient;
import org.jetbrains.annotations.NotNull;

public record StoneCuttingRecipeData(@NotNull String group, @NotNull Ingredient ingredient,
                                     ItemStack result) implements RecipeData {
}
