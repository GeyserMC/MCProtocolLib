package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;

@Builder(toBuilder = true)
public record Weapon(int itemDamagePerAttack, boolean canDisableBlocking) {
}
