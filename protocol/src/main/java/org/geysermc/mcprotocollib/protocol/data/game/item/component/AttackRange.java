package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;

@Builder(toBuilder = true)
public record AttackRange(float minRange, float maxRange, float hitboxMargin, float mobFactor) {
}
