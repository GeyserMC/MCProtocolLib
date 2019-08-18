package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemStack {
    private final int id;
    private final int amount;
    private final CompoundTag nbt;

    public ItemStack(int id) {
        this(id, 1);
    }

    public ItemStack(int id, int amount) {
        this(id, amount, null);
    }
}
