package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.Nullable;

public record ResolvableInt(boolean isConstant, int value, @Nullable Key key) {
}
