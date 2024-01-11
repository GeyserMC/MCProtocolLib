package com.github.steveice10.mc.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class DataComponents {
    private final Map<DataComponentType<?>, DataComponent<?, ?>> dataComponents;

    @Nullable
    public <T> T get(DataComponentType<T> type) {
        DataComponent component = dataComponents.get(type);
        return component == null ? null : (T) component.getValue();
    }

    public <T> T getOrDefault(DataComponentType<T> type, T def) {
        T value = get(type);
        return value != null ? value : def;
    }

    public <T> void put(DataComponentType<T> type, @NonNull T value) {
        if (type instanceof IntComponentType intType) {
            dataComponents.put(intType, intType.primitiveFactory.createPrimitive(intType, (Integer) value));
        } else if (type instanceof BooleanComponentType boolType) {
            dataComponents.put(boolType, boolType.primitiveFactory.createPrimitive(boolType, (Boolean) value));
        } else {
            dataComponents.put(type, type.dataComponentFactory.create(type, value));
        }
    }

    public DataComponents clone() {
        return new DataComponents(new HashMap<>(dataComponents));
    }
}
