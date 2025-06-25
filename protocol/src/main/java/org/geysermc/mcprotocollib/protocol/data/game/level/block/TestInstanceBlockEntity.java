package org.geysermc.mcprotocollib.protocol.data.game.level.block;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.math.vector.Vector3i;
import org.jetbrains.annotations.Nullable;

public record TestInstanceBlockEntity(@Nullable Key test, Vector3i size, int rotation, boolean ignoreEntities,
                                      int status, @Nullable Component errorMessage) {
}
