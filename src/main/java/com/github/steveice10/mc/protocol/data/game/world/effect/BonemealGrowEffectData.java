package com.github.steveice10.mc.protocol.data.game.world.effect;

public class BonemealGrowEffectData implements WorldEffectData {
    private int particleCount;

    public BonemealGrowEffectData(int particleCount) {
        this.particleCount = particleCount;
    }

    public int getParticleCount() {
        return this.particleCount;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BonemealGrowEffectData && this.particleCount == ((BonemealGrowEffectData) o).particleCount;
    }

    @Override
    public int hashCode() {
        return this.particleCount;
    }
}
