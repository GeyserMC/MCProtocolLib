package com.github.steveice10.mc.protocol.data.game.level.map;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class MapIcon {
    private final int centerX;
    private final int centerZ;
    private final @NotNull MapIconType iconType;
    private final int iconRotation;
    private final Component displayName;
}
