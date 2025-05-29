package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.data.game.entity.EquipmentSlot;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;

@Builder(toBuilder = true)
public record Equippable(EquipmentSlot slot, Sound equipSound, @Nullable Key model, @Nullable Key cameraOverlay,
                         @Nullable HolderSet allowedEntities, boolean dispensable, boolean swappable, boolean damageOnHurt,
                         boolean equipOnInteract, boolean canBeSheared, Sound shearingSound) {
}
