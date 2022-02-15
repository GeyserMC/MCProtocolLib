package com.github.steveice10.mc.protocol.data.game.entity.metadata.type;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.FloatMetadataType;
import com.github.steveice10.packetlib.io.NetOutput;
import lombok.NonNull;

import java.io.IOException;

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
    protected void write(NetOutput out) throws IOException {
        this.type.writeMetadataPrimitive(out, this.value);
    }
}
