package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;

import java.util.Map;

@Builder(toBuilder = true)
public record ArmorTrim(Holder<TrimMaterial> material, Holder<TrimPattern> pattern) {

    @Builder(toBuilder = true)
    public record TrimMaterial(String assetBase, Map<Key, String> assetOverrides, Component description) {
        public TrimMaterial(String assetBase, Map<Key, String> assetOverrides, Component description) {
            this.assetBase = assetBase;
            this.assetOverrides = Map.copyOf(assetOverrides);
            this.description = description;
        }
    }

    @Builder(toBuilder = true)
    public record TrimPattern(Key assetId, Component description, boolean decal) {
    }
}
