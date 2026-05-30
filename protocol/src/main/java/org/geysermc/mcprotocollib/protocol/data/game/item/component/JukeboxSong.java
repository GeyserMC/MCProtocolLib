package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;

@Builder(toBuilder = true)
public record JukeboxSong(Sound soundEvent, Component description, float lengthInSeconds, int comparatorOutput) {
}
