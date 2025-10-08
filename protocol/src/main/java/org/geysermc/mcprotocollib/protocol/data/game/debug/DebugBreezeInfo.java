package org.geysermc.mcprotocollib.protocol.data.game.debug;

import org.cloudburstmc.math.vector.Vector3i;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;

public record DebugBreezeInfo(OptionalInt attackTarget, @Nullable Vector3i jumpTarget) implements DebugInfo {
    @Override
    public DebugSubscriptions getType() {
        return DebugSubscriptions.BREEZES;
    }
}
