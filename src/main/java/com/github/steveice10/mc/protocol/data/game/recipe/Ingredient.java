package com.github.steveice10.mc.protocol.data.game.recipe;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public record Ingredient(ItemStack @NotNull [] options) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient that)) return false;
        return Arrays.deepEquals(options, that.options);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(options);
    }
}
