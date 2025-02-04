package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.Map;

@Data
@With
@Builder(toBuilder = true)
public class ItemEnchantments {
    private final Map<Integer, Integer> enchantments;
    private final boolean showInTooltip;

    public ItemEnchantments(Map<Integer, Integer> enchantments, boolean showInTooltip) {
        this.enchantments = Map.copyOf(enchantments);
        this.showInTooltip = showInTooltip;
    }
}
