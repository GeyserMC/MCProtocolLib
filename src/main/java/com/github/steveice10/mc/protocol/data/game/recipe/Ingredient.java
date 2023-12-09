package com.github.steveice10.mc.protocol.data.game.recipe;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public record Ingredient(@Nullable ItemStack @NonNull [] options) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Arrays.equals(options, that.options);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(options);
    }
}
