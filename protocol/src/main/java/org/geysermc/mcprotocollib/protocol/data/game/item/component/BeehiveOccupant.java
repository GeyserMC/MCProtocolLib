package com.github.steveice10.mc.protocol.data.game.item.component;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BeehiveOccupant {
    private final CompoundTag entityData;
    private final int ticksInHive;
    private final int minTicksInHive;
}
