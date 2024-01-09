package org.geysermc.mcprotocollib.protocol.data.game.level.notify;

import lombok.Data;

@Data
public class ElderGuardianEffectValue implements GameEventValue {
    private final boolean audible;

    public ElderGuardianEffectValue(float data) {
        this.audible = Math.round(data) == 1;
    }
}
