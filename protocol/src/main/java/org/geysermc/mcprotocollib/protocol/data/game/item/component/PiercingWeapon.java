package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;
import org.jetbrains.annotations.Nullable;

@Builder(toBuilder = true)
public record PiercingWeapon(float minReach, float maxReach, float hitboxMargin, boolean dealsKnockback,
                             boolean dismounts, @Nullable Sound sound, @Nullable Sound hitSound) {
}
