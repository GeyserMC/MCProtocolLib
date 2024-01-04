package org.geysermc.mc.protocol.data.game.recipe.data;

import org.geysermc.mc.protocol.data.game.entity.metadata.ItemStack;
import org.geysermc.mc.protocol.data.game.recipe.CraftingBookCategory;
import org.geysermc.mc.protocol.data.game.recipe.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@Data
@AllArgsConstructor
public class ShapelessRecipeData implements RecipeData {
    private final @NonNull String group;
    private final @NonNull CraftingBookCategory category;
    private final @NonNull Ingredient[] ingredients;
    private final @Nullable ItemStack result;
}
