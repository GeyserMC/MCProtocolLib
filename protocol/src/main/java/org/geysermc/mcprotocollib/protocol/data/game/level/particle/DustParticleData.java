package org.geysermc.mcprotocollib.protocol.data.game.level.particle;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DustParticleData implements ParticleData {
    private final int color;
    private final float scale; // clamped between 0.01 and 4
}
