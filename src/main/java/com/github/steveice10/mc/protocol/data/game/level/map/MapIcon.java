package com.github.steveice10.mc.protocol.data.game.level.map;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public record MapIcon(int centerX, int centerZ,
                      @NotNull MapIconType iconType, int iconRotation,
                      Component displayName) {
}
