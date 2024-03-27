package com.github.steveice10.mc.protocol.data.game.recipe;

import com.github.steveice10.mc.protocol.data.game.item.ItemStack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class Ingredient {
    private final @Nullable ItemStack @NonNull [] options;
}
