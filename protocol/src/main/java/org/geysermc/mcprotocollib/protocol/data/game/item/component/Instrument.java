package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;

@Data
@AllArgsConstructor
public class Instrument {
    private final Sound soundEvent;
    private final int useDuration;
    private final float range;
}
