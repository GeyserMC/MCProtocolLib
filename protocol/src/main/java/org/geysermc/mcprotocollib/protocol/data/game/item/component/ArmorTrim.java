package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;

public record ArmorTrim(Holder<TrimMaterial> material, Holder<TrimPattern> pattern, boolean showInTooltip) {
    public record TrimMaterial(String assetName, int ingredientId, float itemModelIndex,
                               Int2ObjectMap<String> overrideArmorMaterials, Component description) {
    }

    public record TrimPattern(String assetId, int templateItemId, Component description, boolean decal) {
    }
}
