package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record CustomModelData(List<Float> floats, List<Boolean> flags, List<String> strings, List<Integer> colors) {
    public CustomModelData(List<Float> floats, List<Boolean> flags, List<String> strings, List<Integer> colors) {
        this.floats = List.copyOf(floats);
        this.flags = List.copyOf(flags);
        this.strings = List.copyOf(strings);
        this.colors = List.copyOf(colors);
    }
}
