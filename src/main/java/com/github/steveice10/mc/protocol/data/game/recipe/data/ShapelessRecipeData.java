package com.github.steveice10.mc.protocol.data.game.recipe.data;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.recipe.CraftingBookCategory;
import com.github.steveice10.mc.protocol.data.game.recipe.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class ShapelessRecipeData implements RecipeData {
    private final @NotNull String group;
    private final @NotNull CraftingBookCategory category;
    private final @NotNull Ingredient[] ingredients;
    private final ItemStack result;
}
