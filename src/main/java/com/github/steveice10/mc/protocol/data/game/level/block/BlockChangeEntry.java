package com.github.steveice10.mc.protocol.data.game.level.block;

import org.cloudburstmc.math.vector.Vector3i;
import org.jetbrains.annotations.NotNull;

public record BlockChangeEntry(@NotNull Vector3i position, int block) {
}
