package com.github.steveice10.mc.protocol.data.game.recipe.data;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.recipe.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class SmithingTransformRecipeData implements RecipeData {
    private final @NotNull Ingredient template;
    private final @NotNull Ingredient base;
    private final @NotNull Ingredient addition;
    private final ItemStack result;
}
