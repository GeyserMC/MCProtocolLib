package org.geysermc.mcprotocollib.protocol.data.game.level.particle;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShriekParticleData implements ParticleData {
    private final int delay;
}

