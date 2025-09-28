package org.geysermc.mcprotocollib.protocol.data.game.debug;

import org.cloudburstmc.math.vector.Vector3i;

public record DebugPoiInfo(Vector3i pos, int poiType, int freeTicketCount) implements DebugInfo {
    @Override
    public DebugSubscriptions getType() {
        return DebugSubscriptions.POIS;
    }
}
