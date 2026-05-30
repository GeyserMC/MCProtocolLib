package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;

@Builder(toBuilder = true)
public record AttackRange(float minReach, float maxReach, float minCreativeReach,
                          float maxCreativeReach, float hitboxMargin, float mobFactor) {
}
