package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

@Builder(toBuilder = true)
public record SignText(List<Component> messages, @Nullable List<Component> filteredMessages,
                       int color, boolean hasGlowingText) {
}
