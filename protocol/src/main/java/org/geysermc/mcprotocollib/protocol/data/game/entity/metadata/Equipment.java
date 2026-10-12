package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.data.game.entity.EquipmentSlot;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Data
@AllArgsConstructor
public class Equipment {
    private EquipmentSlot slot;
    private @Nullable ItemStack item;
}
