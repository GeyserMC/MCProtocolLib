package org.geysermc.mcprotocollib.protocol.data.game.debug;

import org.cloudburstmc.math.vector.Vector3i;

import java.util.List;

public record DebugPathInfo(Path path, float maxNodeDistance) implements DebugInfo {
    @Override
    public DebugSubscriptions getType() {
        return DebugSubscriptions.ENTITY_PATHS;
    }

    public record Path(boolean reached, int nextNodeIndex, Vector3i target, List<Node> nodes,
                       DebugData debugData) {
        public record DebugData(List<Node> targetNodes, Node[] openSet, Node[] closedSet) {}
    }

    public record Node(int x, int y, int z, float walkedDistance, float costMalus,
                       boolean closed, PathType type, float f) {}

    public enum PathType {
        BLOCKED,
        OPEN,
        WALKABLE,
        WALKABLE_DOOR,
        TRAPDOOR,
        POWDER_SNOW,
        DANGER_POWDER_SNOW,
        FENCE,
        LAVA,
        WATER,
        WATER_BORDER,
        RAIL,
        UNPASSABLE_RAIL,
        DANGER_FIRE,
        DAMAGE_FIRE,
        DANGER_OTHER,
        DAMAGE_OTHER,
        DOOR_OPEN,
        DOOR_WOOD_CLOSED,
        DOOR_IRON_CLOSED,
        BREACH,
        LEAVES,
        STICKY_HONEY,
        COCOA,
        DAMAGE_CAUTIOUS,
        DANGER_TRAPDOOR;

        private static final PathType[] VALUES = values();

        public static PathType from(int id) {
            return VALUES[id];
        }
    }
}
