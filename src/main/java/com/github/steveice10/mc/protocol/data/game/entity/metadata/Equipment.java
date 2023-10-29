package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.mc.protocol.data.game.entity.EquipmentSlot;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class Equipment {
    private @NotNull EquipmentSlot slot;
    private ItemStack item;
}
