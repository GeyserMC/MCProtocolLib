package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import net.kyori.adventure.key.Key;
import org.cloudburstmc.math.vector.Vector3i;

public class GlobalPos {
    private final Key dimension;
    private final Vector3i position;

    public GlobalPos(Key dimension, Vector3i position) {
        this.dimension = dimension;
        this.position = position;
    }

    public Key getDimension() {
        return dimension;
    }

    public Vector3i getPosition() {
        return position;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public int getZ() {
        return position.getZ();
    }
}
