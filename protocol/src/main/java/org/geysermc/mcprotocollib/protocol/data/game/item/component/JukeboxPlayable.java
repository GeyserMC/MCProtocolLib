package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;
import org.jetbrains.annotations.Nullable;

@Builder(toBuilder = true)
public record JukeboxPlayable(@Nullable Holder<JukeboxSong> songHolder, @Nullable Key songLocation, boolean showInTooltip) {

    @Builder(toBuilder = true)
    public record JukeboxSong(Sound soundEvent, Component description, float lengthInSeconds, int comparatorOutput) {
    }
}
