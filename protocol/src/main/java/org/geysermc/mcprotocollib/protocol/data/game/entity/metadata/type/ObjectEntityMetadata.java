package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type;

import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.EntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.MetadataType;

public class ObjectEntityMetadata<T> extends EntityMetadata<T, MetadataType<T>> {
    private final T value;

    public ObjectEntityMetadata(int id, MetadataType<T> type, T value) {
        super(id, type);
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }
}
