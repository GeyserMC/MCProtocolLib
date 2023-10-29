package com.github.steveice10.mc.protocol.data.game.entity.metadata.type;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataType;
import org.jetbrains.annotations.NotNull;

public class ObjectEntityMetadata<T> extends EntityMetadata<T, MetadataType<T>> {
    private final T value;

    public ObjectEntityMetadata(int id, @NotNull MetadataType<T> type, T value) {
        super(id, type);
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }
}
