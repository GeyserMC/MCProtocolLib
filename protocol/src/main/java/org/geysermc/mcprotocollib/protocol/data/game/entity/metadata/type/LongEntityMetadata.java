package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.NonNull;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.EntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.LongMetadataType;

public class LongEntityMetadata extends EntityMetadata<Long, LongMetadataType> {
    private final long value;

    public LongEntityMetadata(int id, @NonNull LongMetadataType type, long value) {
        super(id, type);
        this.value = value;
    }

    public long getPrimitiveValue() {
        return this.value;
    }

    @Override
    @Deprecated
    public Long getValue() {
        return this.value;
    }

    @Override
    public void write(MinecraftByteBuf out) {
        this.type.writeMetadataPrimitive(out, value);
    }
}
