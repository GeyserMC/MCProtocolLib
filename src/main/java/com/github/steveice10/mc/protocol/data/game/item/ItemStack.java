package com.github.steveice10.mc.protocol.data.game.item;

import com.github.steveice10.mc.protocol.data.game.item.component.DataComponentPatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class ItemStack {
    private final int id;
    private final int amount;
    private final @Nullable DataComponentPatch dataComponentPatch;

    public ItemStack(int id) {
        this(id, 1);
    }

    public ItemStack(int id, int amount) {
        this(id, amount, null);
    }
}
