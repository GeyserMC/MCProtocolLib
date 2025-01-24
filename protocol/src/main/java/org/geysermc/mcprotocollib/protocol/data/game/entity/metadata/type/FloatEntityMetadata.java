package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type;

import io.netty.buffer.ByteBuf;
import lombok.NonNull;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.EntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.FloatMetadataType;

public class FloatEntityMetadata extends EntityMetadata<Float, FloatMetadataType> {
    private final float value;

    public FloatEntityMetadata(int id, @NonNull FloatMetadataType type, float value) {
        super(id, type);
        this.value = value;
    }

    public float getPrimitiveValue() {
        return this.value;
    }

    @Override
    @Deprecated
    public Float getValue() {
        return this.value;
    }

    @Override
    public void write(ByteBuf out) {
        this.type.writeMetadataPrimitive(out, this.value);
    }
}
