package com.github.steveice10.mc.protocol.data.game.world.map;

public class MapIcon {
    private int centerX;
    private int centerZ;
    private MapIconType iconType;
    private int iconRotation;

    public MapIcon(int centerX, int centerZ, MapIconType iconType, int iconRotation) {
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.iconType = iconType;
        this.iconRotation = iconRotation;
    }

    public int getCenterX() {
        return this.centerX;
    }

    public int getCenterZ() {
        return this.centerZ;
    }

    public MapIconType getIconType() {
        return this.iconType;
    }

    public int getIconRotation() {
        return this.iconRotation;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MapIcon && this.centerX == ((MapIcon) o).centerX && this.centerZ == ((MapIcon) o).centerZ && this.iconType == ((MapIcon) o).iconType && this.iconRotation == ((MapIcon) o).iconRotation;
    }

    @Override
    public int hashCode() {
        int result = this.centerX;
        result = 31 * result + this.centerZ;
        result = 31 * result + this.iconRotation;
        result = 31 * result + (this.iconType != null ? this.iconType.hashCode() : 0);
        return result;
    }
}
