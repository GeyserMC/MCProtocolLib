package com.github.steveice10.mc.protocol.data.game.recipe.data;

import com.github.steveice10.mc.protocol.data.game.item.ItemStack;
import com.github.steveice10.mc.protocol.data.game.recipe.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class StoneCuttingRecipeData implements RecipeData {
    private final @NonNull String group;
    private final @NonNull Ingredient ingredient;
    private final @Nullable ItemStack result;
}
