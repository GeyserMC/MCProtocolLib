package com.github.steveice10.mc.protocol.data.game.recipe;

import com.github.steveice10.mc.protocol.data.game.recipe.data.RecipeData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Recipe {
    private final @NonNull RecipeType type;
    private final @NonNull String identifier;
    private final RecipeData data;
}
