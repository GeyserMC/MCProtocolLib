package com.github.steveice10.mc.protocol.data.game.world.particle;

import com.github.steveice10.mc.protocol.util.ObjectUtil;

public class DustParticleData implements ParticleData {
    private final float red; // 0 - 1
    private final float green; // 0 - 1
    private final float blue; // 0 - 1
    private final float scale; // clamped between 0.01 and 4

    public DustParticleData(float red, float green, float blue, float scale) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.scale = scale;
    }

    public float getRed() {
        return this.red;
    }

    public float getGreen() {
        return this.green;
    }

    public float getBlue() {
        return this.blue;
    }

    public float getScale() {
        return this.scale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DustParticleData)) return false;

        DustParticleData that = (DustParticleData) o;
        return Float.compare(that.red, this.red) == 0 &&
                Float.compare(that.green, this.green) == 0 &&
                Float.compare(that.blue, this.blue) == 0 &&
                Float.compare(that.scale, this.scale) == 0;
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.red, this.green, this.blue, this.scale);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
