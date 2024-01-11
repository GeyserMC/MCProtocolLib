package org.geysermc.mcprotocollib.protocol.data.game.level.particle;

import org.geysermc.mcprotocollib.protocol.data.game.level.particle.positionsource.PositionSource;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VibrationParticleData implements ParticleData {
    PositionSource positionSource;

    int arrivalTicks;
}
