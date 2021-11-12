package com.github.steveice10.mc.protocol.data.game.level.particle;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DustParticleData implements ParticleData {
    private final float red; // 0 - 1
    private final float green; // 0 - 1
    private final float blue; // 0 - 1
    private final float scale; // clamped between 0.01 and 4
}
