package org.geysermc.mcprotocollib.protocol.data.game.debug;

public enum DebugSubscriptions {
    DEDICATED_SERVER_TICK_TIME,
    BEES,
    BRAINS,
    BREEZES,
    GOAL_SELECTORS,
    ENTITY_PATHS,
    ENTITY_BLOCK_INTERSECTIONS,
    BEE_HIVES,
    POIS,
    REDSTONE_WIRE_ORIENTATIONS,
    VILLAGE_SECTIONS,
    RAIDS,
    STRUCTURES,
    GAME_EVENT_LISTENERS,
    NEIGHBOR_UPDATES,
    GAME_EVENTS;

    private static final DebugSubscriptions[] VALUES = values();

    public static DebugSubscriptions from(int id) {
        return VALUES[id];
    }
}
