package com.github.steveice10.mc.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class DataComponentPatch {
    private final Map<DataComponentType<?>, DataComponent<?, ?>> dataComponents;

    public <T> DataComponent<T, DataComponentType<T>> get(DataComponentType<T> type) {
        return (DataComponent<T, DataComponentType<T>>) dataComponents.get(type);
    }

    public <T> void put(DataComponentType<T> type, T value) {
        dataComponents.put(type, type.dataComponentFactory.create(type, value));
    }
}
