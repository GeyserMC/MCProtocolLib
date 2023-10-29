package com.github.steveice10.mc.protocol.data.game.level.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.cloudburstmc.math.vector.Vector3i;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class BlockChangeEntry {
    private final @NotNull Vector3i position;
    private final int block;
}
