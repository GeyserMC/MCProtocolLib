package org.geysermc.mcprotocollib.protocol.data.game.level.waypoint;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public record TrackedWaypoint(@Nullable UUID uuid, @Nullable String id, Icon icon, Type type, @Nullable WaypointData data) {

    public record Icon(Key style, Optional<Integer> color) {
    }

    public enum Type {
        EMPTY,
        VEC3I,
        CHUNK,
        AZIMUTH;

        private static final Type[] VALUES = values();

        public static Type from(int id) {
            return VALUES[id];
        }
    }
}
