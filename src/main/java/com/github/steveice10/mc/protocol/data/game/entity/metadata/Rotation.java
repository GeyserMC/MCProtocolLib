package com.github.steveice10.mc.protocol.data.game.entity.metadata;

public class Rotation {
    private float pitch;
    private float yaw;
    private float roll;

    public Rotation() {
        this(0, 0, 0);
    }

    public Rotation(float pitch, float yaw, float roll) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getRoll() {
        return this.roll;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Rotation && Float.compare(this.pitch, ((Rotation) o).pitch) == 0 && Float.compare(this.yaw, ((Rotation) o).yaw) == 0 && Float.compare(this.roll, ((Rotation) o).roll) == 0);
    }

    @Override
    public int hashCode() {
        int result = Float.floatToIntBits(this.pitch);
        result = 31 * result + Float.floatToIntBits(this.yaw);
        result = 31 * result + Float.floatToIntBits(this.roll);
        return result;
    }
}
