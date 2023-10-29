package com.github.steveice10.mc.protocol.data.game.recipe;

import com.github.steveice10.mc.protocol.data.game.recipe.data.RecipeData;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class Recipe {
    private final @NotNull RecipeType type;
    private final @NotNull String identifier;
    private final RecipeData data;
}
