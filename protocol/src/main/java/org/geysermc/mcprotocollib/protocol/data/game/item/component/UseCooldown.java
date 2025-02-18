package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.With;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.Nullable;

@With
public record UseCooldown(float seconds, @Nullable Key cooldownGroup) {
}
