package com.github.steveice10.mc.protocol.data.game.world.particle;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlockParticleData implements ParticleData {
    private final int blockState;
}
