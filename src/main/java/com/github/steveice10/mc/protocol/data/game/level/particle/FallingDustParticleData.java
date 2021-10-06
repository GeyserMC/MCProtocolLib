package com.github.steveice10.mc.protocol.data.game.level.particle;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FallingDustParticleData implements ParticleData {
    private final int blockState;
}
