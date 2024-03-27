package com.github.steveice10.mc.protocol.data.game.item.component;

import com.github.steveice10.mc.protocol.data.game.level.sound.Sound;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Instrument {
    private final int instrumentId;
    private final Sound soundEvent;
    private final int useDuration;
    private final float range;
}
