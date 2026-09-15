package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;

@Builder(toBuilder = true)
public record MobVisibility(HolderSet targetingEntityTypes, float visibility) {
}
