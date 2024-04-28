package org.geysermc.mcprotocollib.protocol.data.game.level.particle;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.positionsource.PositionSource;

@Data
@AllArgsConstructor
public class VibrationParticleData implements ParticleData {
    PositionSource positionSource;

    int arrivalTicks;
}
