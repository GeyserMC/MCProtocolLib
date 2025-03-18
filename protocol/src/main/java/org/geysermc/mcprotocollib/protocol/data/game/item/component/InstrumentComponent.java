package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;
import org.jetbrains.annotations.Nullable;


public record InstrumentComponent(@Nullable Holder<Instrument> instrumentHolder, @Nullable Key instrumentLocation) {

    public record Instrument(Sound soundEvent, float useDuration, float range, Component description) {
    }
}
