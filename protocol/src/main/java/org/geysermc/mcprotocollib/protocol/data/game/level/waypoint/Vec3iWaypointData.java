package org.geysermc.mcprotocollib.protocol.data.game.level.waypoint;

import org.cloudburstmc.math.vector.Vector3i;

public record Vec3iWaypointData(Vector3i vector) implements WaypointData {
}
