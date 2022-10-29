package com.github.steveice10.mc.protocol.data.game.level.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.cloudburstmc.math.vector.Vector3i;

@Data
@AllArgsConstructor
public class BlockChangeEntry {
    private final @NonNull Vector3i position;
    private final int block;
}
