package com.github.steveice10.mc.protocol.data.game.level.block;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import org.jetbrains.annotations.Nullable;

public record BlockEntityInfo(int x, int y, int z, BlockEntityType type, @Nullable CompoundTag nbt) {
}
