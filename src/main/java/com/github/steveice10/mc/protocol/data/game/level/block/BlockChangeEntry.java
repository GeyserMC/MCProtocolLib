package com.github.steveice10.mc.protocol.data.game.level.block;

import org.cloudburstmc.math.vector.Vector3i;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class BlockChangeEntry {
    private final @NonNull Vector3i position;
    private final int block;
}
