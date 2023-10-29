package com.github.steveice10.mc.protocol.data.game.recipe;

import com.github.steveice10.mc.protocol.data.game.recipe.data.RecipeData;
import org.jetbrains.annotations.NotNull;

public record Recipe(@NotNull RecipeType type, @NotNull String identifier, RecipeData data) {
}
