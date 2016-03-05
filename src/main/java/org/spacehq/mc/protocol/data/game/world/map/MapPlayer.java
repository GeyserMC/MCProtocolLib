package org.spacehq.mc.protocol.data.game.world.map;

public class MapPlayer {
    private int centerX;
    private int centerZ;
    private int iconSize;
    private int iconRotation;

    public MapPlayer(int centerX, int centerZ, int iconSize, int iconRotation) {
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.iconSize = iconSize;
        this.iconRotation = iconRotation;
    }

    public int getCenterX() {
        return this.centerX;
    }

    public int getCenterZ() {
        return this.centerZ;
    }

    public int getIconSize() {
        return this.iconSize;
    }

    public int getIconRotation() {
        return this.iconRotation;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MapPlayer && this.centerX == ((MapPlayer) o).centerX && this.centerZ == ((MapPlayer) o).centerZ && this.iconSize == ((MapPlayer) o).iconSize && this.iconRotation == ((MapPlayer) o).iconRotation;
    }

    @Override
    public int hashCode() {
        int result = this.centerX;
        result = 31 * result + this.centerZ;
        result = 31 * result + this.iconRotation;
        result = 31 * result + this.iconSize;
        return result;
    }
}
