package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.data.game.ResourceLocation;

public class GlobalPos {
    private final ResourceLocation dimension;
    private final Vector3i position;

    public GlobalPos(ResourceLocation dimension, Vector3i position) {
        this.dimension = dimension;
        this.position = position;
    }

    public ResourceLocation getDimension() {
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
