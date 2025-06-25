package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Data;
import lombok.With;

import java.util.Map;

@Data
@With
public class ItemEnchantments {
    private final Map<Integer, Integer> enchantments;

    public ItemEnchantments(Map<Integer, Integer> enchantments) {
        this.enchantments = Map.copyOf(enchantments);
    }
}
