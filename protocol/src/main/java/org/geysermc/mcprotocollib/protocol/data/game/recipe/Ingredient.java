package org.geysermc.mcprotocollib.protocol.data.game.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;

@Data
@AllArgsConstructor
public class Ingredient {
    private final @Nullable ItemStack @NonNull [] options;
}
