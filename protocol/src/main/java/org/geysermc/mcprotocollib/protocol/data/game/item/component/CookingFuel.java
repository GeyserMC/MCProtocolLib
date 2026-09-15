package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import net.kyori.adventure.key.Key;

@Builder(toBuilder = true)
public record CookingFuel(Key burnTime, Key speedMultiplier) {
}
