package com.github.steveice10.mc.protocol.data.game.recipe.data;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.recipe.Ingredient;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public record SmithingTransformRecipeData(@NonNull Ingredient template, @NonNull Ingredient base, @NonNull Ingredient addition, @Nullable ItemStack result) implements RecipeData {
}
