package org.geysermc.mcprotocollib.protocol.data.game.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponents;

@Data
@AllArgsConstructor
public class ItemStack {
    private final int id;
    private final int amount;
    private final @Nullable DataComponents dataComponents;

    public ItemStack(int id) {
        this(id, 1);
    }

    public ItemStack(int id, int amount) {
        this(id, amount, null);
    }
}
