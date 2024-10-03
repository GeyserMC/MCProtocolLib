package org.geysermc.mcprotocollib.protocol.data.game.recipe.data;

import org.geysermc.mcprotocollib.protocol.data.game.Holder;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.CraftingBookCategory;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.Ingredient;

public record TransmuteRecipeData(String group, CraftingBookCategory category, Ingredient input, Ingredient material, int result) implements RecipeData {
}
