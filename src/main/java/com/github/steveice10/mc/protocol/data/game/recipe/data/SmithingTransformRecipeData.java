package com.github.steveice10.mc.protocol.data.game.recipe.data;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.recipe.Ingredient;
import org.jetbrains.annotations.NotNull;

public record SmithingTransformRecipeData(@NotNull Ingredient template, @NotNull Ingredient base,
                                          @NotNull Ingredient addition, ItemStack result) implements RecipeData {
}
