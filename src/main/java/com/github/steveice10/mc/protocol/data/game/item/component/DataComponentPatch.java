package com.github.steveice10.mc.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Data
@AllArgsConstructor
public class DataComponentPatch {
    private final Map<DataComponentType<?>, DataComponent<?, ?>> dataComponents;

    @Nullable
    public <T> T get(DataComponentType<T> type) {
        DataComponent component = dataComponents.get(type);
        return component == null ? null : (T) component.getValue();
    }

    public <T> void put(DataComponentType<T> type, @NonNull T value) {
        dataComponents.put(type, type.dataComponentFactory.create(type, value));
    }
}
