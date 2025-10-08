package org.geysermc.mcprotocollib.protocol.data.game.debug;

import org.cloudburstmc.math.vector.Vector3i;

import java.util.List;

public record DebugBrainDump(String name, String profession, int xp, float health, float maxHealth,
                             String inventory, boolean wantsGolem, int angerLevel, List<String> activities,
                             List<String> behaviors, List<String> memories, List<String> gossips,
                             List<Vector3i> pois, List<Vector3i> potentialPois) implements DebugInfo {
    @Override
    public DebugSubscriptions getType() {
        return DebugSubscriptions.BRAINS;
    }
}
