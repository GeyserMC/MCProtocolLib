package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import org.cloudburstmc.math.vector.Vector3i;

public class GlobalPos {
    private final String dimension;
    private final Vector3i position;

    public GlobalPos(String dimension, Vector3i position) {
        this.dimension = dimension;
        this.position = position;
    }

    public String getDimension() {
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
