package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.GlobalPos;
import org.jetbrains.annotations.Nullable;

@Data
@With
@AllArgsConstructor
@Builder(toBuilder = true)
public class LodestoneTracker {
    private final @Nullable GlobalPos pos;
    private final boolean tracked;
}
