package com.github.steveice10.mc.protocol.data.game.world.map;

import com.github.steveice10.mc.protocol.data.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class MapIcon {
    private final int centerX;
    private final int centerZ;
    private final @NonNull MapIconType iconType;
    private final int iconRotation;
    private final Message displayName;
}
