package com.github.steveice10.mc.protocol.data.game.recipe.data;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.recipe.CraftingBookCategory;
import com.github.steveice10.mc.protocol.data.game.recipe.Ingredient;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public record ShapedRecipeData(int width, int height, @NonNull String group, @NonNull CraftingBookCategory category, @NonNull Ingredient[] ingredients, @Nullable ItemStack result, boolean showNotification) implements RecipeData {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShapedRecipeData that = (ShapedRecipeData) o;
        return width == that.width && height == that.height && showNotification == that.showNotification && Objects.equals(group, that.group) && category == that.category && Arrays.equals(ingredients, that.ingredients) && Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        int result1 = Objects.hash(width, height, group, category, result, showNotification);
        result1 = 31 * result1 + Arrays.hashCode(ingredients);
        return result1;
    }
}
