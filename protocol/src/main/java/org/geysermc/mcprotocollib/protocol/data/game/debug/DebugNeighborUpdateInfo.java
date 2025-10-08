package org.geysermc.mcprotocollib.protocol.data.game.debug;

import org.cloudburstmc.math.vector.Vector3i;

public record DebugNeighborUpdateInfo(Vector3i pos) implements DebugInfo {
    @Override
    public DebugSubscriptions getType() {
        return DebugSubscriptions.NEIGHBOR_UPDATES;
    }
}
