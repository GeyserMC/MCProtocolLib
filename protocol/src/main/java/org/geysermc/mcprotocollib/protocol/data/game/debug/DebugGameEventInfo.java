package org.geysermc.mcprotocollib.protocol.data.game.debug;

import org.cloudburstmc.math.vector.Vector3i;

public record DebugGameEventInfo(int eventId, Vector3i pos) implements DebugInfo {
    @Override
    public DebugSubscriptions getType() {
        return DebugSubscriptions.GAME_EVENTS;
    }
}
