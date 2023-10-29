package com.github.steveice10.mc.protocol.data.game.recipe.data;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.recipe.CraftingBookCategory;
import com.github.steveice10.mc.protocol.data.game.recipe.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public record ShapelessRecipeData(@NotNull String group, @NotNull CraftingBookCategory category,
                                  @NotNull Ingredient[] ingredients, ItemStack result) implements RecipeData {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShapelessRecipeData that)) return false;
        return Objects.equals(group, that.group) && category == that.category && Arrays.deepEquals(ingredients, that.ingredients) && Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        int result1 = Objects.hash(group, category, result);
        result1 = 31 * result1 + Arrays.deepHashCode(ingredients);
        return result1;
    }
}
