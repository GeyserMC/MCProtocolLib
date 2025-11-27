package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import org.cloudburstmc.nbt.NbtMap;

import java.util.Map;

@Builder(toBuilder = true)
public record DataComponentMatchers(Map<DataComponentType<?>, DataComponent<?, ?>> exactMatchers, Map<PredicateType, NbtMap> partialMatchers) {
    public DataComponentMatchers(Map<DataComponentType<?>, DataComponent<?, ?>> exactMatchers, Map<PredicateType, NbtMap> partialMatchers) {
        this.exactMatchers = Map.copyOf(exactMatchers);
        this.partialMatchers = Map.copyOf(partialMatchers);
    }

    @Builder(toBuilder = true)
    public record PredicateType(boolean isPredicate, int id) {
    }
}
