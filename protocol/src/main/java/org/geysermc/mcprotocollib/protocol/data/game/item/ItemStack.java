<<<<<<<< HEAD:protocol/src/main/java/org/geysermc/mcprotocollib/protocol/data/game/item/ItemStack.java
package com.github.steveice10.mc.protocol.data.game.item;
========
package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;
>>>>>>>> 4f2e7a5b (Move package/license to GeyserMC, gradle conventions and submodules (#782)):protocol/src/main/java/org/geysermc/mcprotocollib/protocol/data/game/entity/metadata/ItemStack.java

import com.github.steveice10.mc.protocol.data.game.item.component.DataComponents;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.Nullable;

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
