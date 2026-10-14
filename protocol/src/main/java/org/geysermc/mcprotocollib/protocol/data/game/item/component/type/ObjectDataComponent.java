package org.geysermc.mcprotocollib.protocol.data.game.item.component.type;

import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponent;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponentType;
import org.jspecify.annotations.Nullable;

public class ObjectDataComponent<T> extends DataComponent<T, DataComponentType<T>> {
    private final @Nullable T value;

    public ObjectDataComponent(DataComponentType<T> type, @Nullable T value) {
        super(type);
        this.value = value;
    }

    @Override
    public @Nullable T getValue() {
        return value;
    }
}
