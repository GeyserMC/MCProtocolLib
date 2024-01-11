package com.github.steveice10.mc.protocol.data.game.item.component.type;

import com.github.steveice10.mc.protocol.data.game.item.component.DataComponent;
import com.github.steveice10.mc.protocol.data.game.item.component.DataComponentType;
import lombok.NonNull;

public class ObjectDataComponent<T> extends DataComponent<T, DataComponentType<T>> {
    private final T value;

    public ObjectDataComponent(@NonNull DataComponentType<T> type, T value) {
        super(type);
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }
}
