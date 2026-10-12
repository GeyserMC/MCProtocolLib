package org.geysermc.mcprotocollib.protocol.data.game.level.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.cloudburstmc.nbt.NbtMap;
import org.jspecify.annotations.Nullable;

@Data
@AllArgsConstructor
public class BlockEntityInfo {
    private final int x;
    private final int y;
    private final int z;
    private final BlockEntityType type;
    private final @Nullable NbtMap nbt;
}
