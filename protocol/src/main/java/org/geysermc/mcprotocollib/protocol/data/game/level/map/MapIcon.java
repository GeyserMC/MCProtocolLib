package org.geysermc.mcprotocollib.protocol.data.game.level.map;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

@Data
@AllArgsConstructor
public class MapIcon {
    private final int centerX;
    private final int centerZ;
    private final MapIconType iconType;
    private final int iconRotation;
    private final @Nullable Component displayName;
}
