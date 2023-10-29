package com.github.steveice10.mc.protocol.data.game.recipe.data;

import com.github.steveice10.mc.protocol.data.game.recipe.Ingredient;
import org.jetbrains.annotations.NotNull;

public record SmithingTrimRecipeData(@NotNull Ingredient template, @NotNull Ingredient base,
                                     @NotNull Ingredient addition) implements RecipeData {
}
