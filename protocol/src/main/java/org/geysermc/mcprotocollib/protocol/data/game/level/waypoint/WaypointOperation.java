package org.geysermc.mcprotocollib.protocol.data.game.level.waypoint;

public enum WaypointOperation {
    TRACK,
    UNTRACK,
    UPDATE;

    private static final WaypointOperation[] VALUES = values();

    public static WaypointOperation from(int id) {
        return VALUES[Math.floorMod(id, VALUES.length)];
    }
}
