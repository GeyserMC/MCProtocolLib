package org.geysermc.mcprotocollib.protocol.data.game.level.particle;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Data
@AllArgsConstructor
public class ItemParticleData implements ParticleData {
    private final @Nullable ItemStack itemStack;
}
