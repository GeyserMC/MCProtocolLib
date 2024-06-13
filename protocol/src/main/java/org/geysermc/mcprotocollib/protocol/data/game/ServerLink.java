package org.geysermc.mcprotocollib.protocol.data.game;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public record ServerLink(@Nullable ServerLinkType knownType, @Nullable Component unknownType, String link) {
}
