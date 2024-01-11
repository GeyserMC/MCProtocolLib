package com.github.steveice10.mc.protocol.data.game.item.component;

import com.github.steveice10.mc.protocol.data.game.Holder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.Component;

public record ArmorTrim(Holder<TrimMaterial> material, Holder<TrimPattern> pattern, boolean showInTooltip) {
    public record TrimMaterial(String assetName, int ingredientId, float itemModelIndex,
                               Int2ObjectMap<String> overrideArmorMaterials, Component description) {
    }

    public record TrimPattern(String assetId, int templateItemId, Component description, boolean decal) {
    }
}
