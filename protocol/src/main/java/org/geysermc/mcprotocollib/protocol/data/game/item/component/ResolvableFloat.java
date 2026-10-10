package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.Nullable;

public record ResolvableFloat(boolean isConstant, float value, @Nullable Key key) {
}
