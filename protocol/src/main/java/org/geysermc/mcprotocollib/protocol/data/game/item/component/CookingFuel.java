package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;

@Builder(toBuilder = true)
public record CookingFuel(ResolvableInt burnTime, ResolvableFloat speedMultiplier) {
}
