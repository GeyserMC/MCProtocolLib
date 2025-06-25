package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;

import java.util.Map;

@Builder(toBuilder = true)
public record DataComponentMatchers(Map<DataComponentType<?>, DataComponent<?, ?>> exactMatchers, int[] partialMatchers) {
}
