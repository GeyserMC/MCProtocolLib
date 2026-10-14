package org.geysermc.mcprotocollib.protocol.data.game.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponents;
import org.jspecify.annotations.Nullable;

@Data
@AllArgsConstructor
public class ItemStack {
    private final int id;
    private final int amount;
    private final @Nullable DataComponents dataComponentsPatch;

    public ItemStack(int id) {
        this(id, 1);
    }

    public ItemStack(int id, int amount) {
        this(id, amount, null);
    }
}
