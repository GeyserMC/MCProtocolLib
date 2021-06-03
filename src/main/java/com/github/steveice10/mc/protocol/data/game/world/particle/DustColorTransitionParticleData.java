package com.github.steveice10.mc.protocol.data.game.world.particle;

import lombok.Getter;

@Getter
public class DustColorTransitionParticleData extends DustParticleData {
    private final double newRed;
    private final double newGreen;
    private final double newBlue;

    public DustColorTransitionParticleData(double red, double green, double blue, float scale, double newRed, double newGreen, double newBlue) {
        super(red, green, blue, scale);
        this.newRed = newRed;
        this.newGreen = newGreen;
        this.newBlue = newBlue;
    }
}
