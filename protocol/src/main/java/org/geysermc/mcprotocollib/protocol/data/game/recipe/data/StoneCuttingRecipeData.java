package org.geysermc.mcprotocollib.protocol.data.game.recipe.data;

import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@Data
@AllArgsConstructor
public class StoneCuttingRecipeData implements RecipeData {
    private final @NonNull String group;
    private final @NonNull Ingredient ingredient;
    private final @Nullable ItemStack result;
}
