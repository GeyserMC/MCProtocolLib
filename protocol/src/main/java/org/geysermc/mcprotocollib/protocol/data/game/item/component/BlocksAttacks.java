package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Builder(toBuilder = true)
public record BlocksAttacks(float blockDelaySeconds, float disableCooldownScale, List<DamageReduction> damageReductions,
                            ItemDamageFunction itemDamage, @Nullable Key bypassedBy, @Nullable Sound blockSound, @Nullable Sound disableSound) {
    public BlocksAttacks(float blockDelaySeconds, float disableCooldownScale, List<DamageReduction> damageReductions,
                         ItemDamageFunction itemDamage, @Nullable Key bypassedBy, @Nullable Sound blockSound, @Nullable Sound disableSound) {
        this.blockDelaySeconds = blockDelaySeconds;
        this.disableCooldownScale = disableCooldownScale;
        this.damageReductions = List.copyOf(damageReductions);
        this.itemDamage = itemDamage;
        this.bypassedBy = bypassedBy;
        this.blockSound = blockSound;
        this.disableSound = disableSound;
    }

    @Builder(toBuilder = true)
    public record DamageReduction(float horizontalBlockingAngle, @Nullable HolderSet type, float base, float factor) {
    }

    @Builder(toBuilder = true)
    public record ItemDamageFunction(float threshold, float base, float factor) {
    }
}
