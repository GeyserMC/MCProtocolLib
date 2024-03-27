package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.mc.protocol.data.game.entity.EquipmentSlot;
import com.github.steveice10.mc.protocol.data.game.item.ItemStack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class Equipment {
    private @NonNull EquipmentSlot slot;
    private @Nullable ItemStack item;
}
