package org.geysermc.mcprotocollib.protocol.data.game.recipe.display;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.HolderSet;

import java.util.List;
import java.util.OptionalInt;

public record RecipeDisplayEntry(int id, RecipeDisplay display, OptionalInt group,
                                 int category, @Nullable List<HolderSet> craftingRequirements) {
}
