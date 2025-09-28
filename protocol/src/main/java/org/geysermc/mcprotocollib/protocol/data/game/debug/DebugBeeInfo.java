package org.geysermc.mcprotocollib.protocol.data.game.debug;

import org.cloudburstmc.math.vector.Vector3i;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record DebugBeeInfo(@Nullable Vector3i hivePos, @Nullable Vector3i flowerPos, int travelTicks,
                           List<Vector3i> blacklistedHives) implements DebugInfo {
    @Override
    public DebugSubscriptions getType() {
        return DebugSubscriptions.BEES;
    }
}
