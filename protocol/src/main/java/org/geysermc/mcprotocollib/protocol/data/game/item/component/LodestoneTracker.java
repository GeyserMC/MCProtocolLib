package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.GlobalPos;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class LodestoneTracker {
    private final @Nullable GlobalPos pos;
    private final boolean tracked;
}
