package com.github.steveice10.mc.protocol.data.game.level.particle;

import lombok.Getter;

@Getter
public class DustColorTransitionParticleData extends DustParticleData {
    private final float newRed;
    private final float newGreen;
    private final float newBlue;

    public DustColorTransitionParticleData(float red, float green, float blue, float scale, float newRed, float newGreen, float newBlue) {
        super(red, green, blue, scale);
        this.newRed = newRed;
        this.newGreen = newGreen;
        this.newBlue = newBlue;
    }
}
