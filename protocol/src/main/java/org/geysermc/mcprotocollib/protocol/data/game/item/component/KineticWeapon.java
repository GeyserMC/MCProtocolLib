package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;
import org.jetbrains.annotations.Nullable;

@Builder(toBuilder = true)
public record KineticWeapon(int contactCooldownTicks, int delayTicks, @Nullable Condition dismountConditions,
                            @Nullable Condition knockbackConditions, @Nullable Condition damageConditions,
                            float forwardMovement, float damageMultiplier, @Nullable Sound sound, @Nullable Sound hitSound) {

    @Builder(toBuilder = true)
    public record Condition(int maxDurationTicks, float minSpeed, float minRelativeSpeed) {
    }
}
