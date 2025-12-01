package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;

@Builder(toBuilder = true)
public record UseEffects(boolean canSprint, boolean interactVibrations, float speedMultiplier) {
}
