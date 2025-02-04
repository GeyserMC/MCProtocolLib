package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
@Builder(toBuilder = true)
public class DyedItemColor {
    private final int rgb;
    private final boolean showInTooltip;
}
