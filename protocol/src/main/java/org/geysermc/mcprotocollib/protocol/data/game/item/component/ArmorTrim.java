package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import lombok.With;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;

import java.util.Map;

@With
@Builder(toBuilder = true)
public record ArmorTrim(Holder<TrimMaterial> material, Holder<TrimPattern> pattern, boolean showInTooltip) {

    @With
    @Builder(toBuilder = true)
    public record TrimMaterial(String assetName, int ingredientId, Map<Key, String> overrideArmorAssets, Component description) {
        public TrimMaterial(String assetName, int ingredientId, Map<Key, String> overrideArmorAssets, Component description) {
            this.assetName = assetName;
            this.ingredientId = ingredientId;
            this.overrideArmorAssets = Map.copyOf(overrideArmorAssets);
            this.description = description;
        }
    }

    @With
    @Builder(toBuilder = true)
    public record TrimPattern(Key assetId, int templateItemId, Component description, boolean decal) {
    }
}
