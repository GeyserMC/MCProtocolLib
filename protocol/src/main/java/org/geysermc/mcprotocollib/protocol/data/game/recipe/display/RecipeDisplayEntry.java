package org.geysermc.mcprotocollib.protocol.data.game.recipe.display;

import org.geysermc.mcprotocollib.protocol.data.game.item.component.HolderSet;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.OptionalInt;

public record RecipeDisplayEntry(int id, RecipeDisplay display, OptionalInt group,
                                 int category, @Nullable List<HolderSet> craftingRequirements) {
}
