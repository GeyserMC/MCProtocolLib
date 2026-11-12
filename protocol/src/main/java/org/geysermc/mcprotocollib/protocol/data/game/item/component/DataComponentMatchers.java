package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;

import java.util.Map;

@Builder(toBuilder = true)
public record DataComponentMatchers(Map<DataComponentType<?>, DataComponent<?, ?>> exactMatchers, Map<PredicateType, ?> partialMatchers) {
    public DataComponentMatchers(Map<DataComponentType<?>, DataComponent<?, ?>> exactMatchers, Map<PredicateType, ?> partialMatchers) {
        this.exactMatchers = Map.copyOf(exactMatchers);
        this.partialMatchers = Map.copyOf(partialMatchers);
    }

    @Builder(toBuilder = true)
    public record PredicateType(boolean isPredicate, int id) {
    }
}
