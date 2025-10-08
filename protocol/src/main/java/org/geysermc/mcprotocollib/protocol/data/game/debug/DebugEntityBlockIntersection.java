package org.geysermc.mcprotocollib.protocol.data.game.debug;

public enum DebugEntityBlockIntersection implements DebugInfo {
    IN_BLOCK,
    IN_FLUID,
    IN_AIR;

    @Override
    public DebugSubscriptions getType() {
        return DebugSubscriptions.ENTITY_BLOCK_INTERSECTIONS;
    }

    private static final DebugEntityBlockIntersection[] VALUES = values();

    public static DebugEntityBlockIntersection from(int id) {
        return id >= 0 && id < VALUES.length ? VALUES[id] : VALUES[0];
    }
}
