package com.github.steveice10.mc.protocol.data.game.recipe.data;

import com.github.steveice10.mc.protocol.data.game.recipe.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class SmithingTrimRecipeData implements RecipeData {
    private final @NonNull Ingredient template;
    private final @NonNull Ingredient base;
    private final @NonNull Ingredient addition;
}
