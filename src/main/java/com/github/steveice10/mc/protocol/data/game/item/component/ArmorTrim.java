package com.github.steveice10.mc.protocol.data.game.item.component;

import com.github.steveice10.mc.protocol.data.game.Holder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.Component;

@Data
@AllArgsConstructor
public class ArmorTrim {
    private final Holder<TrimMaterial> material;
    private final Holder<TrimPattern> pattern;
    private final boolean showInTooltip;

    @Data
    @AllArgsConstructor
    public static class TrimMaterial {
        private final String assetName;
        private final int ingredientId;
        private final float itemModelIndex;
        private final Int2ObjectMap<String> overrideArmorMaterials;
        private final Component description;
    }

    @Data
    @AllArgsConstructor
    public static class TrimPattern {
        private final String assetId;
        private final int templateItemId;
        private final Component description;
        private final boolean decal;
    }
}
