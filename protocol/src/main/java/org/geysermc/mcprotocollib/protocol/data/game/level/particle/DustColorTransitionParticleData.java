package org.geysermc.mcprotocollib.protocol.data.game.level.particle;

import lombok.Getter;

@Getter
public class DustColorTransitionParticleData extends DustParticleData {
    private final int newColor;

    public DustColorTransitionParticleData(int color, float scale, int newColor) {
        super(color, scale);
        this.newColor = newColor;
    }
}
