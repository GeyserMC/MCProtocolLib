package org.geysermc.mcprotocollib.protocol.data.game;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;

public record ServerLink(OptionalInt knownType, @Nullable Component unknownType, String url) {
}
