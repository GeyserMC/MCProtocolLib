package org.geysermc.mcprotocollib.protocol.data.game.item.component.type;

import lombok.NonNull;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponent;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponentType;

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
