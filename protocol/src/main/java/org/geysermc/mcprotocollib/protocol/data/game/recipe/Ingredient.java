package org.geysermc.mcprotocollib.protocol.data.game.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.HolderSet;

@Data
@AllArgsConstructor
public class Ingredient {
    private final HolderSet values;
}
