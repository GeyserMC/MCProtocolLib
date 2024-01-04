package org.geysermc.mc.protocol.data.game.entity.metadata;

import org.geysermc.mc.protocol.data.game.entity.EquipmentSlot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@Data
@AllArgsConstructor
public class Equipment {
    private @NonNull EquipmentSlot slot;
    private @Nullable ItemStack item;
}
