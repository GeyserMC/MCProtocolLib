package org.geysermc.mcprotocollib.protocol.data.game.level.block;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.Nullable;

@Data
@AllArgsConstructor
public class BlockEntityInfo {
    private final int x;
    private final int y;
    private final int z;
    private final BlockEntityType type;
    private final @Nullable CompoundTag nbt;
}
