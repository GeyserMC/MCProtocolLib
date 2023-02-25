package com.github.steveice10.mc.protocol.data.game.recipe.data;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.recipe.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class SmithingTransformRecipeData implements RecipeData {
    private final @NonNull Ingredient template;
    private final @NonNull Ingredient base;
    private final @NonNull Ingredient addition;
    private final ItemStack result;
}
