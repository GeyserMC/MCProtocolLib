package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.With;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.Nullable;

@With
public record UseCooldown(float seconds, @Nullable Key cooldownGroup) {
}
