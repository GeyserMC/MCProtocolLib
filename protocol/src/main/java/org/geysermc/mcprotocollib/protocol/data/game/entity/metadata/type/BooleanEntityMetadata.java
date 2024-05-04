package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type;

import lombok.NonNull;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.BooleanMetadataType;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.EntityMetadata;

public class BooleanEntityMetadata extends EntityMetadata<Boolean, BooleanMetadataType> {
    private final boolean value;

    public BooleanEntityMetadata(int id, @NonNull BooleanMetadataType type, boolean value) {
        super(id, type);
        this.value = value;
    }

    public boolean getPrimitiveValue() {
        return this.value;
    }

    @Override
    @Deprecated
    public Boolean getValue() {
        return this.value;
    }

    @Override
    public void write(MinecraftByteBuf out) {
        this.type.writeMetadataPrimitive(out, this.value);
    }
}
