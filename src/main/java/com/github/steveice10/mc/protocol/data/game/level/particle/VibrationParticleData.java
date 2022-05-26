package com.github.steveice10.mc.protocol.data.game.level.particle;

import com.github.steveice10.mc.protocol.data.game.level.particle.positionsource.PositionSource;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VibrationParticleData implements ParticleData {
    PositionSource positionSource;

    int arrivalTicks;
}
