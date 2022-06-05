package com.github.steveice10.mc.protocol.data.game.level.block;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class BlockEntityInfo {
    private final int x;
    private final int y;
    private final int z;
    private final BlockEntityType type;
    private final @Nullable CompoundTag nbt;
}
