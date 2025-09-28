package org.geysermc.mcprotocollib.protocol.data.game.debug;

import org.cloudburstmc.math.vector.Vector3i;

import java.util.List;

public record DebugRaidsInfo(List<Vector3i> positions) implements DebugInfo {
    @Override
    public DebugSubscriptions getType() {
        return DebugSubscriptions.RAIDS;
    }
}
