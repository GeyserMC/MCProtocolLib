package com.github.steveice10.mc.protocol.data.game.world.particle;

import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class FallingDustParticleData implements ParticleData {
    private final @NonNull BlockState blockState;
}
