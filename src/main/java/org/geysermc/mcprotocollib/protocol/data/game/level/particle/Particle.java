package org.geysermc.mcprotocollib.protocol.data.game.level.particle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Particle {
    private final @NonNull ParticleType type;
    private final ParticleData data;
}
