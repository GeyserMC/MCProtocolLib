package org.geysermc.mc.protocol.data.game.level.particle;

import org.geysermc.mc.protocol.data.game.entity.metadata.ItemStack;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.Nullable;

@Data
@AllArgsConstructor
public class ItemParticleData implements ParticleData {
    private final @Nullable ItemStack itemStack;
}
