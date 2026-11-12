package org.geysermc.mcprotocollib.protocol.data.game.debug;

import org.cloudburstmc.math.vector.Vector3d;
import org.cloudburstmc.math.vector.Vector3i;

public record DebugGameEventInfo(int eventId, Vector3d pos) implements DebugInfo {
    public DebugGameEventInfo(int eventId, Vector3i pos) {
        this(eventId, pos.toDouble());
    }

    @Override
    public DebugSubscriptions getType() {
        return DebugSubscriptions.GAME_EVENTS;
    }
}
