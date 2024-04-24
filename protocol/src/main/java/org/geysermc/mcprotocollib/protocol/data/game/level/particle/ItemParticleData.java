package org.geysermc.mcprotocollib.protocol.data.game.level.particle;

import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.Nullable;

@Data
@AllArgsConstructor
public class ItemParticleData implements ParticleData {
    private final @Nullable ItemStack itemStack;
}
