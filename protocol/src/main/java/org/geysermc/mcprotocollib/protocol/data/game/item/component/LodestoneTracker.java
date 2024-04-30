package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.GlobalPos;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class LodestoneTracker {
    private final @Nullable GlobalPos pos;
    private final boolean tracked;
}
