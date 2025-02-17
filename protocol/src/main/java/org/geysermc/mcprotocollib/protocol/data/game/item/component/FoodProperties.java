package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
@Builder(toBuilder = true)
public class FoodProperties {
    private final int nutrition;
    private final float saturationModifier;
    private final boolean canAlwaysEat;
}
