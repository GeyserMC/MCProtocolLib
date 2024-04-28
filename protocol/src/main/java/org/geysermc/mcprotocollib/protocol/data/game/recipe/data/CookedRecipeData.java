package org.geysermc.mcprotocollib.protocol.data.game.recipe.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.CraftingBookCategory;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.Ingredient;

@Data
@AllArgsConstructor
public class CookedRecipeData implements RecipeData {
    private final @NonNull String group;
    private final @NonNull CraftingBookCategory category;
    private final @NonNull Ingredient ingredient;
    private final @Nullable ItemStack result;
    private final float experience;
    private final int cookingTime;
}
