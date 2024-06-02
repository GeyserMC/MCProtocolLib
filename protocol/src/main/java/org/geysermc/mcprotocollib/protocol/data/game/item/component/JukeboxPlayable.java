package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;
import org.jetbrains.annotations.Nullable;

public record JukeboxPlayable(@Nullable Holder<JukeboxSong> songHolder, @Nullable String songLocation, boolean showInTooltip) {
    public record JukeboxSong(Sound soundEvent, Component description, float lengthInSeconds, int comparatorOutput) {
    }
}
