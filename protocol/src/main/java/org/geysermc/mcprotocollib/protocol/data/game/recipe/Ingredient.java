package org.geysermc.mcprotocollib.protocol.data.game.recipe;

import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@Data
@AllArgsConstructor
public class Ingredient {
    private final @Nullable ItemStack @NonNull [] options;
}
