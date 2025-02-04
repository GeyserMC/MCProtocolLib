package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type;

import io.netty.buffer.ByteBuf;
import lombok.NonNull;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.EntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.IntMetadataType;

public class IntEntityMetadata extends EntityMetadata<Integer, IntMetadataType> {
    private final int value;

    public IntEntityMetadata(int id, @NonNull IntMetadataType type, int value) {
        super(id, type);
        this.value = value;
    }

    public int getPrimitiveValue() {
        return this.value;
    }

    @Override
    @Deprecated
    public Integer getValue() {
        return this.value;
    }

    @Override
    public void write(ByteBuf out) {
        this.type.writeMetadataPrimitive(out, value);
    }
}
